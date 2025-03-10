package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.UserService;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Controller for the user detail view.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("view")
public class UserDetailController implements Serializable {

    @Autowired
    private transient UserService userService;

    /**
     * Attribute to cache the currently displayed user
     */
    private Userx user;

    private List<String> selectedRoles;

    @PostConstruct
    public void init() {
        this.user = null;
    }

    /**
     * Sets the currently displayed user and reloads it form db. This user is
     * targeted by any further calls of
     * {@link #doReloadUser()}, {@link #doSaveUser()} and
     * {@link #doDeleteUser()}.
     *
     * @param user
     */
    public void setUser(Userx user) {
        this.user = user;
        doReloadUser();
    }

    /**
     * Returns the currently displayed user.
     *
     * @return
     */
    public Userx getUser() {
        return user;
    }

    /**
     * Action to force a reload of the currently displayed user.
     */
    public void doReloadUser() {
        user = userService.loadUser(user.getUsername());
    }

    /**
     * Action to save the currently displayed user.
     */
    public void doSaveUser() {
        user = this.userService.saveUser(user);
    }

    /**
     * Action to delete the currently displayed user.
     */
    public void doDeleteUser() {
        this.userService.deleteUser(user);
        user = null;
    }

    public void createUser() {
        user = new Userx();

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();

        user.setFirstName(request.getParameter("createForm:firstNameCreation"));
        user.setLastName(request.getParameter("createForm:lastNameCreation"));
        user.setEmail(request.getParameter("createForm:mailCreation"));
        user.setId(request.getParameter("createForm:usernameCreation"));
        user.setPassword(new BCryptPasswordEncoder(10).encode(request.getParameter("createForm:passwordCreation")));
        user.setEnabled(true);

        Set<UserRole> roles = new HashSet<>();
        if (selectedRoles != null) {
            for (String role : selectedRoles) {
                switch (role) {
                    case "ADMIN" -> roles.add(UserRole.ADMIN);
                    case "GARDENER" -> roles.add(UserRole.GARDENER);
                    default -> roles.add(UserRole.USER);
                }
            }
        }
        user.setRoles(roles);
        try {
            user = this.userService.saveUser(user);
            init();
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Could not create user", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public List<String> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

}
