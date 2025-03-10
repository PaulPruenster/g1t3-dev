package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.UserService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Scope("session")
public class UserEditBean {
    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private UserService userService;

    private Userx userx;

    @PostConstruct
    public void init() {
        this.userx = sessionInfoBean.getCurrentUser();
    }

    public void saveUser() {
        userService.saveUser(userx);
        FacesMessage msg = new FacesMessage("Password successfully changed");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public Userx getUserx() {
        return userx;
    }

    public void setUserx(Userx userx) {
        this.userx = userx;
    }
}
