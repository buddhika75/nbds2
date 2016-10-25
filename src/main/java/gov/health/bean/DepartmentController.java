package gov.health.bean;

import gov.health.entity.Area;
import gov.health.entity.Department;
import gov.health.entity.Institution;
import gov.health.facade.DepartmentFacade;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("departmentController")
@SessionScoped
public class DepartmentController implements Serializable {

    @EJB
    DepartmentFacade ejbFacade;

    @Inject
    SessionController sessionController;

    Department removing;
    private Department current;
    private List<Department> items = null;
    String selectText = "";

    Institution institution;
    String depaertmentName;

    List<Department> insDepts;

    boolean selectControlDisable = false;
    boolean modifyControlDisable = true;

    private DepartmentFacade getFacade() {
        return ejbFacade;
    }

    public void fillInsDepts() {
        String sql;
        Map m = new HashMap();
        sql = "select d from Department d where d.retired=false and d.institution=:ins order by d.name";
        m.put("ins", institution);
        insDepts = getFacade().findBySQL(sql, m);
    }

    public void addDepartmentForInstitution() {
        if (institution == null) {
            gov.health.bean.JsfUtil.addErrorMessage("Please select the Institution");
            return;
        }
        if (depaertmentName.trim().equals("")) {
            gov.health.bean.JsfUtil.addErrorMessage("Please enter the name of the Department");
            return;
        }
        Department departmentAdd = new Department();
        departmentAdd.setInstitution(institution);
        departmentAdd.setName(depaertmentName);
        getFacade().create(departmentAdd);
        depaertmentName = "";
        fillInsDepts();
        gov.health.bean.JsfUtil.addSuccessMessage("Saved");

        
    }

    
//    public void saveSelected() {
//        if (current == null) {
//            gov.health.bean.JsfUtil.addErrorMessage("Nothing to save");
//            return;
//        }
//
//        if (current.getId() != null && current.getId() != 0) {
//            getFacade().edit(current);
//            gov.health.bean.JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedOldSuccessfully"));
//        } else {
//            current.setCreatedAt(Calendar.getInstance().getTime());
//            current.setCreater(sessionController.getLoggedUser());
//            getFacade().create(current);
//
//            gov.health.bean.JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
//        }
//
//        getItems();
//        selectText = "";
//    }
//    
    
    public void prepareAdd() {
        current = new Department();

    }

    public void removeDepartment() {
        if (removing == null) {
            gov.health.bean.JsfUtil.addErrorMessage("Nothing to Delete");
            return;
        }
        removing.setRetired(true);
        getFacade().edit(removing);
        gov.health.bean.JsfUtil.addErrorMessage("Removed");
        fillInsDepts();
        removing = null;
    }

    public void prepareModifyControlDisable() {
        selectControlDisable = false;
        modifyControlDisable = true;
    }

    List<Department> mappedDepartment;

    Department currentMappingDepartment;

    public void recreateModel() {
        items = null;
    }

    public void prepareSelect() {
        this.prepareModifyControlDisable();
    }

    public void delete() {

        if (current != null) {
            current.setRetired(true);
            current.setRetiredAt(Calendar.getInstance().getTime());
            current.setRetirer(sessionController.getLoggedUser());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(new MessageProvider().getValue("deleteSuccessful"));
        } else {
            JsfUtil.addErrorMessage(new MessageProvider().getValue("nothingToDelete"));
        }
        
//        if (current != null) {
//            current.setRetired(true);
//            current.setRetiredAt(Calendar.getInstance().getTime());
//            current.setRetirer(sessionController.getLoggedUser());
//            getFacade().edit(current);
//            gov.health.bean.JsfUtil.addSuccessMessage(new MessageProvider().getValue("deleteSuccessful"));
//        } else {
//            gov.health.bean.JsfUtil.addErrorMessage(new MessageProvider().getValue("nothingToDelete"));
//        }
//        
//        recreateModel();
//        getItems();
//        selectText = "";
//        current = null;
//        this.prepareSelect();
        fillInsDepts();
        
    }

    public Department getRemoving() {
        return removing;
    }

    public void setRemoving(Department removing) {
        System.err.println("dep " + removing);
        this.removing = removing;
    }

    public void departmenListen(Department dep) {
        setRemoving(dep);
    }

    public List<Department> getInsDepts() {
        return insDepts;
    }

    public void setInsDepts(List<Department> insDepts) {
        this.insDepts = insDepts;
    }

    public DepartmentFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(DepartmentFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public boolean isSelectControlDisable() {
        return selectControlDisable;
    }

    public void setSelectControlDisable(boolean selectControlDisable) {
        this.selectControlDisable = selectControlDisable;
    }

    public boolean isModifyControlDisable() {
        return modifyControlDisable;
    }

    public void setModifyControlDisable(boolean modifyControlDisable) {
        this.modifyControlDisable = modifyControlDisable;
    }

    public List<Department> getMappedDepartment() {
        return mappedDepartment;
    }

    public void setMappedDepartment(List<Department> mappedDepartment) {
        this.mappedDepartment = mappedDepartment;
    }

    public Department getCurrentMappingDepartment() {
        return currentMappingDepartment;
    }

    public void setCurrentMappingDepartment(Department currentMappingDepartment) {
        this.currentMappingDepartment = currentMappingDepartment;
    }

    public void saveDepartment(Department ins) {
        if (ins == null) {
            gov.health.bean.JsfUtil.addErrorMessage("Nothing to update");
            return;
        }
        if (ins.getId() == null || ins.getId() == 0) {
            getFacade().create(ins);
            gov.health.bean.JsfUtil.addSuccessMessage("Saved");
        } else {
            getFacade().edit(ins);
            gov.health.bean.JsfUtil.addSuccessMessage("Updated");
        }
    }

    public Department findDepartment(String insName, boolean createNew) {
        insName = insName.trim();
        if (insName.equals("")) {
            return null;
        }
        Department ins = getFacade().findFirstBySQL("select d from Department d where d.retired = false and lower(d.name) = '" + insName.toLowerCase() + "'");
        if (ins == null && createNew == true) {
            ins = new Department();
            ins.setName(insName);
            ins.setCreatedAt(Calendar.getInstance().getTime());
            ins.setCreater(sessionController.getLoggedUser());
            getFacade().create(ins);
        }
        return ins;
    }

    public DepartmentController() {

    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public String getDepaertmentName() {
        return depaertmentName;
    }

    public void setDepaertmentName(String depaertmentName) {
        this.depaertmentName = depaertmentName;
    }

    public Department getCurrent() {
        if (current == null) {
            current = new Department();
        }
        return current;
    }

    public void setCurrent(Department current) {
        if (this.current != current) {
        }
        this.current = current;
    }

    public List<Department> getItems() {
//        String temSql;
//        //if (items != null) {
//        //    return items;
//        // }
//        // if (getSelectText().equals("")) {
//
//        temSql = "SELECT i FROM Department i where i.retired=false order by i.name";
//
//        //} else {
//        //        temSql = "SELECT i FROM Department i where i.retired=false and LOWER(i.name) like '%" + getSelectText().toLowerCase() + "%' order by i.name";
//        // }
//        items = getFacade().findBySQL(temSql);
//        System.out.println(temSql);
//        return items;
        
        
        items = getFacade().findAll("name", true);
        return items;
    }

    public void setItems(List<Department> items) {
        this.items = items;
    }

    public static int intValue(long value) {
        int valueInt = (int) value;
        if (valueInt != value) {
            throw new IllegalArgumentException(
                    "The long value " + value + " is not within range of the int type");
        }
        return valueInt;
    }

    public void saveSelected() {
        if (current == null) {
            gov.health.bean.JsfUtil.addErrorMessage("Nothing to save");
            return;
        }

        if (current.getId() != null && current.getId() != 0) {
            getFacade().edit(current);
            gov.health.bean.JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedOldSuccessfully"));
        } else {
            current.setCreatedAt(Calendar.getInstance().getTime());
            current.setCreater(sessionController.getLoggedUser());
            getFacade().create(current);

            gov.health.bean.JsfUtil.addSuccessMessage(new MessageProvider().getValue("savedNewSuccessfully"));
        }

        getItems();
        selectText = "";
    }

//    public void delete() {
//
//        if (current != null) {
//            current.setRetired(true);
//            current.setRetiredAt(Calendar.getInstance().getTime());
//            current.setRetirer(sessionController.getLoggedUser());
//            getFacade().edit(current);
//            gov.health.bean.JsfUtil.addSuccessMessage(new MessageProvider().getValue("deleteSuccessful"));
//        } else {
//            gov.health.bean.JsfUtil.addErrorMessage(new MessageProvider().getValue("nothingToDelete"));
//        }
//
//        getItems();
//        selectText = "";
//        current = null;
//
//    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;

    }

    public Department getDepartment(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    public List<Department> completeOfficialDepartments(String qry) {
        String temSql;
        List<Department> dep;
        temSql = "SELECT i FROM Department i where i.retired=false  and LOWER(i.name) like '%" + qry.toLowerCase() + "%' order by i.name";
        dep = getFacade().findBySQL(temSql);
        return dep;
    }

    public List<Department> completeInstitutionDepartments(String qry) {
        String temSql;
        List<Department> dep;
        Map m = new HashMap();
        m.put("ins", institution);
        temSql = "SELECT i FROM Department i where i.retired=false and LOWER(i.name) like '%" + qry.toLowerCase() + "%' and i.institution=:ins order by i.name";
        dep = getFacade().findBySQL(temSql, m);
        System.out.println("m = " + m);
        System.out.println("temSql = " + temSql);
        return dep;
    }

    @FacesConverter(forClass = Department.class)
    public static class DepartmentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DepartmentController controller = (DepartmentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "departmentController");
            return controller.getDepartment(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Department) {
                Department o = (Department) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Department.class.getName());
            }
        }

    }

}
