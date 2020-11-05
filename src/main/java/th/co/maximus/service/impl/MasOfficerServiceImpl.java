package th.co.maximus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.maximus.bean.UserBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.dao.MasOfficerDao;
import th.co.maximus.service.MasOfficerService;

@Service
public class MasOfficerServiceImpl implements MasOfficerService{
	
	@Autowired
	MasOfficerDao masOfficerDao;

	@Override
	public String insertMasOfficerUser(List<UserBean> userBeanList) {
		String statusResult = "";
		try {
			statusResult = Constants.MasterData.STATUS_SUCCESS;
			masOfficerDao.deleteBeforInsertUserRole();
			masOfficerDao.deleteBeforInsert();
			for(int i=0; i<userBeanList.size(); i++) {
//				if(masOfficerDao.selectUserBeanByID(userBeanList.get(i))) {
//					
//				}
				try {
					int idUser = masOfficerDao.insertUserService(userBeanList.get(i));
					long id = userBeanList.get(i).getPrincipal().getId();
//					String roleName = userBeanList.get(i).getPrincipal().getName();
//					int idRole = masOfficerDao.findRoleByRoleName(checkRoleName(roleName));
					int idRole = masOfficerDao.findRoleByRoleName(checkRoleId(id));
					masOfficerDao.insertUserRole(idUser, idRole);
				}catch (Exception ex) {
//					ex.printStackTrace();
					continue;
				}
				
			}
		}catch(Exception e) {
			statusResult = Constants.MasterData.STATUS_FAIL;
			e.printStackTrace();
		}
		return statusResult;
	}
	
	public String checkRoleName(String name) {
		String result = "";
		
		if(Constants.Role.RoleOnline.ADMIN.equalsIgnoreCase(name)) {
			result = Constants.Role.ADMIN;
		}else if(Constants.Role.RoleOnline.SUPPERVISOR.equalsIgnoreCase(name)) {
			result = Constants.Role.SUPPERVISOR;
		}else {
			result = Constants.Role.USER;
		}
		
		return result;
	}
	
	public String checkRoleId(long id) {
		String result = "";
		
		if(Constants.Role.RoleOnline.SUPPERVISOR_8 == (id)) {
			result = Constants.Role.SUPPERVISOR;
		}else {
			result = Constants.Role.USER;
		}
		
		return result;
	}

	@Override
	public void updatePassword(String password, String username) {
		try {
			masOfficerDao.updatePassword(password, username);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
