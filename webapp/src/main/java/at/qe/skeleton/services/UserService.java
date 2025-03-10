package at.qe.skeleton.services;
import at.qe.skeleton.model.SensorStation;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.UserxRepository;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating user data.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("application")
public class UserService {

    @Autowired
    private UserxRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccessPointService accessPointService;

    @Autowired
    private SensorStationService sensorStationService;
    /**
     * Returns a collection of all users.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Userx> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public Userx loadUser(String username) {
        return userRepository.findFirstByUsername(username);
    }

    /**
     * Saves the user. This method will also set {@link Userx#createDate} for new
     * entities or {@link Userx#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link Userx#createDate}
     * or {@link Userx#updateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAnyAuthority('USER') or hasAnyAuthority('GARDENER')")
    public Userx saveUser(Userx user) {
        if (user.isNew()) {
            user.setCreateDate(new Date());
            user.setCreateUser(getAuthenticatedUser());
        } else {
            user.setUpdateDate(new Date());
            user.setUpdateUser(getAuthenticatedUser());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(Userx user) {
        this.accessPointService.getAllAccessPoints();

        for (SensorStation station : sensorStationService.getAllSensorStationsByGaertner(user)){
            sensorStationService.deleteSensorStation(station);
        }
        for (Userx u: this.getAllUsersByCreateUser(user)){
            u.setCreateUser(null);
            this.saveUser(u);
        }
        userRepository.delete(user);
    }

    private Userx getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }


    public Collection<Userx> getAllUsersByRole(UserRole role) {
        return userRepository.findAllByRolesIsContaining(role);
    }

    public Collection<Userx> getAllUsersByCreateUser(Userx user) {
        return userRepository.findAllByCreateUser(user);
    }


}
