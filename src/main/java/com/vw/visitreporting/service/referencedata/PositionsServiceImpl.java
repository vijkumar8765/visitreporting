package com.vw.visitreporting.service.referencedata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dao.referencedata.PositionsDao;
import com.vw.visitreporting.entity.referencedata.Positions;
import com.vw.visitreporting.service.AbstractVRService;

@Service
@Transactional
public class PositionsServiceImpl extends AbstractVRService<Positions> implements PositionsService {

	@Autowired
	private PositionsDao positionsDao;
	
	@Override
	public VRDao<Positions> getTDao() {
		return positionsDao;
	}

	
	/**
	 * Override list function to only return non-disabled meeting types.
	 */
	public List<Positions> list() {
		return positionsDao.findEnabledPositions();
	}

	/**
	 * Delete the Position entity physically from the database. Before deletion check if the Position has any users associated with.
	 * In that case, restrict delete of a position.
	 */
	public String delete(Positions t) {
		if(isPositionUsersAssociated(t)){
			// return back the error message.
			return "Position can not be deleted because of the User association with this position.";
		}
		return positionsDao.delete(t);
	}
	
	public boolean isPositionExists(Positions t){
		return positionsDao.isPositionExists(t);
	}
	
	public Positions save(Positions t){
		// Position sequence is only required when the Position is visible within contact screen,
		// User ideally should not fill in the sequence, but even user does, make it null and persist it. Requirement ref - 6.1.6.4.4
		
		if(!t.getVisibleWithinContactScreen()){
			t.setPositionSequence(null);
		}
		return positionsDao.save(t);
	}
	
	public boolean isPositionUsersAssociated(Positions entity){
		return positionsDao.isPositionUsersAssociated(entity);
	}


	@Override
	public boolean isPositionSequenceExists(Positions entity) {
		return positionsDao.isPositionSequenceExists(entity);
	}


	@Override
	public boolean isPositionDescriptionBeingChanged(Positions entity) {
		return positionsDao.isPositionDescriptionBeingChanged(entity);
	}

}
