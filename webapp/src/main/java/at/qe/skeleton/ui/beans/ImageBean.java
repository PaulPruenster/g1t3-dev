package at.qe.skeleton.ui.beans;

import at.qe.skeleton.api.exceptions.SensorStationNotFoundException;
import at.qe.skeleton.model.SensorStationImage;
import at.qe.skeleton.services.SensorStationImageService;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.utils.ImageUtil;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Component
@Scope("session")
public class ImageBean {

    @Autowired
    private SensorStationImageService sensorStationImageService;

    @Autowired
    private SensorStationService sensorStationService;

    private String imageID;
    private Collection<SensorStationImage> images;
    private List<String> imgStrings;

    public Collection<String> getImgStrings() {
        return imgStrings;
    }

    public void setImgStrings(List<String> imgStrings) {
        this.imgStrings = imgStrings;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        if (imageID != null && !imageID.equals(this.imageID)) {
            getImagesFromId();
            this.imageID = imageID;
            this.refresh();
        }
    }

    public Collection<SensorStationImage> getImages() {
        return images;
    }

    public void setImages(Collection<SensorStationImage> images) {
        this.images = images;
    }

    public void getImagesFromId() {
        if (imageID == null || imageID.equals("")) {
            return;
        }
        this.images = sensorStationImageService.getAllImagesFromStationId(Long.parseLong(imageID));

        this.imgStrings = new ArrayList<>();
        for (SensorStationImage img : this.images) {
            img.setImageData(ImageUtil.decompressImage(img.getImageData()));

            String base64img = Base64.encodeBase64String(img.getImageData());
            this.imgStrings.add("data:" + img.getType() + ";base64," + base64img);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            FacesMessage msg = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            SensorStationImage image = new SensorStationImage();
            image.setImageData(ImageUtil.compressImage(file.getContent()));
            image.setName(file.getFileName());
            image.setType(file.getContentType());
            try {
                image.setSensorStation(sensorStationService.loadSensorStation(Long.parseLong(this.imageID)));
            } catch (SensorStationNotFoundException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sensor station not found"));
            }

            sensorStationImageService.saveSensorStationImage(image);

            this.getImagesFromId();

            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('userEditDialog').hide();");
        }
    }

    public String getUrl() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getRequestURL().toString() + "?id=" + this.imageID;
    }

    public void refresh() {
        this.getImagesFromId();
        PrimeFaces.current().executeScript("location.reload()");
    }

    public void openLogin() {
        String path = "/login.xhtml";
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(path);
        } catch (IOException err) {
            // take the not so clean way to redirect if the other does not work
            PrimeFaces.current().executeScript("window.location.href=%s".formatted(path));
        }
    }
}
