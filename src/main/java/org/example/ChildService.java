package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChildService {
    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private ParentRepository parentRepository;

    protected void update(SomeDto dto) {
        if (!dto.ids.isEmpty()) {
            List<Child> children = dto.ids.stream()
                    .map(id -> childRepository.findById(id).get()).collect(Collectors.toList());

            boolean isValidUpdate = children.stream()
                    .allMatch(i -> isActionValid(i.state, dto.newState));

            if (isValidUpdate) {
                List<Child> updatedChildren = new ArrayList<>();
                updateChildren(children, dto.newState, updatedChildren);

                updatedChildren.forEach(c -> {
                    Parent parent = parentRepository.findById(c.id).get();

                    //some update logic, which is based on current parent state


                    parentRepository.save(parent);
                });
            } else {
                throw new RuntimeException("User is attempting to perform an invalid change.");
            }
        }
    }

    private boolean isActionValid(State from, State to) {
        if (from == State.CREATED) {
            return to == State.CREATED || to == State.UPDATED || to == State.DELETED;
        }

        if (from == State.UPDATED) {
            return to == State.UPDATED || to == State.DELETED;
        }

        if (from == State.DELETED) {
            return to == State.DELETED || to == State.UPDATED;
        }

        return false;
    }

    private List<Child> updateChildren(List<Child> children, State state, List<Child> updated) {
        children.forEach(s -> {
            if (s.state == state) {
                updated.add(s);
            }
            s.state = state;
            childRepository.save(s);
        });

        return updated;
    }

}
