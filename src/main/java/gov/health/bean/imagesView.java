/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.health.bean;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
/**
 *
 * @author root
 */
@ManagedBean
public class imagesView {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    private List<String> images;
     
    @PostConstruct
    public void init() {
        images = new ArrayList<String>();
        for (int i = 1; i <= 8; i++) {
            images.add("P" + i + ".JPG");
        }
    }
 
    public List<String> getImages() {
        return images;
    }
}
