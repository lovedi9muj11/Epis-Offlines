package th.co.maximus.controller;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import th.co.maximus.auth.model.UserProfile;
import th.co.maximus.bean.PaymentMMapPaymentInvBean;
import th.co.maximus.model.OfflineResultModel;
import th.co.maximus.service.CancelPaymentService;
import th.co.maximus.service.ClearingPaymentEpisOfflineService;

@Controller
public class ClearingPaymentEpisOffline {
	@Value("${url.online}")
	private String url;

	private final SSLContext sslContext;
	private final SSLConnectionSocketFactory csf;
	private final HttpComponentsClientHttpRequestFactory requestFactory;
	RestTemplate restTemplate;

	public ClearingPaymentEpisOffline() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
		csf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		requestFactory = new HttpComponentsClientHttpRequestFactory(HttpClients.custom().setSSLSocketFactory(csf).build());
		restTemplate = new RestTemplate(requestFactory);
	}

	@Autowired
	private CancelPaymentService cancelPaymentService;

	@Autowired
	private ClearingPaymentEpisOfflineService clearingPaymentEpisOfflineService;

	@RequestMapping(value = { "/clearing/find" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<PaymentMMapPaymentInvBean> find(@RequestBody PaymentMMapPaymentInvBean creteria) throws Exception {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<>();
//		result = cancelPaymentService.findAllCancelPayments(creteria.getClearing());
		result = cancelPaymentService.findAllCancelPaymentsActive(creteria.getClearing(),"DESC");
		return result;
	}

	@RequestMapping(value = { "/clearing/save" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void save(@RequestBody PaymentMMapPaymentInvBean creteria) throws Exception {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<>();
		result = cancelPaymentService.findAllCancelPayments(creteria.getClearing());
		UserProfile profile = (UserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (result != null) {
			List<OfflineResultModel> objMessage =clearingPaymentEpisOfflineService.callOnlinePayment(result,profile.getUsername());
			for (OfflineResultModel offlineResultModel : objMessage) {
				if (offlineResultModel.getStatus().equals("SUCCESS")) { 
					
					clearingPaymentEpisOfflineService.updateStatusClearing(offlineResultModel.getManualId(),"Y");
				}else {
					clearingPaymentEpisOfflineService.updateStatusClearing(offlineResultModel.getManualId(),"N");
				}
			}
		}

	}
	
}
