package th.co.maximus.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import th.co.maximus.auth.model.UserDto;
import th.co.maximus.auth.service.UserService;
import th.co.maximus.bean.UserBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.dao.MasterDataDao;

@RestController
public class UserController {
	
	@Autowired private UserService userService;
	
	@Autowired
	private MasterDataDao masterDataDao;
	
//	@RequestMapping(value = {"/userManageMent/search"}, method = RequestMethod.POST, produces = "application/json")
//	@ResponseBody
//    public List<UserBean> search(@RequestBody UserDto user) {
//
//		UserDto res = new UserDto();
//		List<UserDto> resList = new ArrayList<UserDto>();
//		List<UserBean> resultList = new ArrayList<UserBean>();
//		
//		UserBean userBean = new UserBean();
//		
//		if("".equals(user.getUsername())) {
//			resList = userService.findAll();
//			}
//		else {res = userService.findByUsername(user.getUsername());resList.add(res);}
//		
//		for(int i=0; i<resList.size(); i++) {
//			String roleCode = "";
//			UserBean result = new UserBean();
//			result.setId(resList.get(i).getId());
//			result.setUserName(resList.get(i).getUsername());
//			for(int j=0; j<resList.get(i).getRoles().size(); j++) {
//				roleCode += resList.get(i).getRoles().get(j).getName() + " ";
//			}
//			result.setRoleCode(roleCode);
//			resultList.add(result);
//		}
//		
//		userBean.setUserBeans(resultList);
//      return resultList;
//    }
	
	@RequestMapping(value = {"/userManageMent/search"}, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public UserBean search(@RequestBody UserDto user) {
		
		UserDto res = new UserDto();
		List<UserDto> resList = new ArrayList<UserDto>();
		List<UserBean> resultList = new ArrayList<UserBean>();
		
		UserBean userBean = new UserBean();
		
		if("".equals(user.getUsername())) {
			resList = userService.findAll();
		}
		else {res = userService.findByUsername(user.getUsername());resList.add(res);}
		
		for(int i=0; i<resList.size(); i++) {
			String roleCode = "";
			UserBean result = new UserBean();
			result.setId(resList.get(i).getId());
			result.setUserName(resList.get(i).getUsername());
			for(int j=0; j<resList.get(i).getRoles().size(); j++) {
				roleCode += resList.get(i).getRoles().get(j).getName() + " ";
			}
			result.setRoleCode(roleCode);
			resultList.add(result);
		}
		
		userBean.setUserBeans(resultList);
		
//		MasterDataBean masterDataBean = new MasterDataBean();
//		masterDataBean = masterDataDao.findGroupTypeByKeyCode("BRANCH_AREA");
		userBean.setCenterServiceName(masterDataDao.findGroupTypeByKeyCode(Constants.INIT_PROJECT_NT_SHOPNAME).getText());
		return userBean;
	}
	
}
