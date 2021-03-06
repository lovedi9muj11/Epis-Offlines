package th.co.maximus;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import th.co.maximus.bean.DeductionManualBean;
import th.co.maximus.dao.DeductionManualDao;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DeductionManualDaoTestCase {
	
	@Autowired
	private DeductionManualDao deductionManualDao;
	
	@Test
	public void findDeductionManualFromManualIdTestCase() {
		List<DeductionManualBean> result = deductionManualDao.findDeductionManualFromManualId(1);
		assertThat(result).isNotEmpty();
	}

}
