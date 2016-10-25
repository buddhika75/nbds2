/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.health.bean;

import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author fhb
 */
@Named(value = "test")
@Dependent
public class Test {

    /**
     * Creates a new instance of Test
     */
    public Test() {
    }
    
}
