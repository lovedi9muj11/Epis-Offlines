package th.co.maximus.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import th.co.maximus.auth.model.GroupTypeDropdown;
import th.co.maximus.auth.model.UserDto;
import th.co.maximus.auth.service.UserService;
import th.co.maximus.bean.UserBean;

@RestController
public class UserController {
	
	private List<GroupTypeDropdown> groupTypeDropdown;
	
	@Autowired private UserService userService;
	
	@RequestMapping(value = {"/userManageMent/search"}, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
    public List<UserBean> search(@RequestBody UserDto user) {

		UserDto res = new UserDto();
		List<UserDto> resList = new ArrayList<UserDto>();
		List<UserBean> resultList = new ArrayList<UserBean>();
		
		if("".equals(user.getUsername())) {
			resList = userService.findAll();
			}
		else {res = userService.findByUsername(user.getUsername());resList.add(res);}
		
		for(int i=0; i<resList.size(); i++) {
			String roleCode = "";
			UserBean result = new UserBean();
			result.setId(resList.get(i).getId());
			result.setUserName(resList.get(i).getUsername());
			result.setPassword(resList.get(i).getPassword());
			for(int j=0; j<resList.get(i).getRoles().size(); j++) {
				roleCode += resList.get(i).getRoles().get(j).getName() + " ";
			}
			result.setRoleCode(roleCode);
			resultList.add(result);
		}
		
      return resultList;
    }
	
	@RequestMapping(value = {"/masterData/selectAll"}, method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
    public List<GroupTypeDropdown> masterData() {
		groupTypeDropdown = new ArrayList<GroupTypeDropdown>();
    	GroupTypeDropdown arg0 = new GroupTypeDropdown();
    	GroupTypeDropdown arg1 = new GroupTypeDropdown();
    	arg0.setName("กรุณาเลือก");
    	arg0.setValue("");
    	arg1.setName("Test");
    	arg1.setValue("T");
    	groupTypeDropdown.add(arg0);
    	groupTypeDropdown.add(arg1);

      return groupTypeDropdown;
    }
}
