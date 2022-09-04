package th.co.maximus.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import th.co.maximus.auth.model.UserProfile;
import th.co.maximus.constants.Constants;

@Repository("ReciptNoGenCode")
public class ReciptNoGenCode {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Value("${text.prefix}")
	private String nameCode;

	
	public String genCodeRecipt(String docType) {
		UserProfile profile = (UserProfile)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 String posNo = profile.getPos();
		 String branArea = profile.getBranchArea();
		StringBuilder sql = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd", Constants.localeEN);
		String date = sdf.format(new Date());
		
		
		int result = 0;
		
		
		SimpleDateFormat datea = new SimpleDateFormat("yyMMdd", Constants.localeEN);
		String dateS = datea.format(new Date());
		
		String dates=convertDateString(dateS);
		try {

			sql.append(" SELECT COUNT(pm.RECEIPT_NO_MANUAL) AS ReciptCount FROM RECEIPT_MANUAL pm ");
			sql.append(" WHERE pm.CREATE_DATE >= ' ").append(convertDateString(date.toString()))
					.append(" 00:00:00.000000' ");
			sql.append(" AND pm.CREATE_DATE <= ' ").append(convertDateString(date.toString()))
					.append(" 23:59:59.999999' ");
			sql.append(" AND pm.DOCTYPE = '").append(docType).append("'");
			result = jdbcTemplate.queryForObject(sql.toString(), Integer.class);
		} catch (Exception e) {
			String zeron = "";
			if(result >8) {
//				zeron ="00"+(result);
				zeron ="00"+(result+1);
			}else {
//				zeron ="000"+(result);
				zeron ="000"+(result+1);
			}
			String codeName = nameCode+branArea+posNo + docType+dates+zeron;		
			return codeName;
		}
		String zeron = "";
		if(result >8) {
//			zeron ="00"+(result);
			zeron ="00"+(result+1);
		}else {
//			zeron ="000"+(result);
			zeron ="000"+(result+1);
		}
		String codeName = nameCode+branArea+posNo + docType+dates+zeron;		
		return codeName;
	}

	public static final String convertDateString(String str) {
		return str.replaceAll("([0-9]{2})/([0-9]{2})/([0-9]{4})", "$3-$2-$1");

	} 

}
