package th.co.maximus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import th.co.maximus.model.TrsMethodManualModel;

@Repository("trsMethodManualRepository")
public interface TrsMethodManualRepository extends JpaRepository<TrsMethodManualModel, Long>{
	
}
