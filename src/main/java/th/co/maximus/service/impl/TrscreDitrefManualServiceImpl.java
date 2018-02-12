package th.co.maximus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.maximus.bean.TrscreDitrefManualBean;
import th.co.maximus.dao.TrscreDitrefManualDao;
import th.co.maximus.service.TrscreDitrefManualService;

@Service
public class TrscreDitrefManualServiceImpl implements TrscreDitrefManualService{
	
	@Autowired TrscreDitrefManualDao trscreDitrefManualDao;

	@Override
	public void insertTrscreDitrefManua(TrscreDitrefManualBean trscreDitrefManualBean) {
		
		trscreDitrefManualDao.insertTrscreDitrefManua(trscreDitrefManualBean);
	}
	
	@Override
	public List<TrscreDitrefManualBean> TrscreDitrefManualAll() {
		//return jdbcTemplate.query("select * from trscreditref_manual", new TrscreDitrefManualJoin());
		return null;
	}

}
