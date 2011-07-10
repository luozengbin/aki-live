package com.appspot.piment.dao;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import net.arnx.jsonic.JSON;

import com.appspot.piment.Constants;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.ConfigItem;
import com.appspot.piment.model.Job;
import com.appspot.piment.model.UserMap;

public class InitDataDao {

  private static final Logger log = Logger.getLogger(Constants.FQCN + InitDataDao.class.getName());

  private PersistenceManager pm = null;

  public InitDataDao() {
	super();
  }

  public void initConfigItem() {
	log.info("initConfigItem -- start");
	restoreDataFromJsonFile(ConfigItem.class, "com/appspot/piment/dao/resource/ConfigItem.json", "key");
	log.info("initConfigItem -- end");
  }

  public void initUserMap() {
	log.info("initUserMap -- start");
	restoreDataFromJsonFile(UserMap.class, "com/appspot/piment/dao/resource/UserMap.json", "id");
	log.info("initUserMap -- end");

  }

  public void initAuthToken() {
	log.info("initAuthToken -- start");
	restoreDataFromJsonFile(AuthToken.class, "com/appspot/piment/dao/resource/AuthToken.json", "userName");
	log.info("initAuthToken -- end");
  }

  public void initJobStatus() {
	log.info("initJobStatus -- start");
	restoreDataFromJsonFile(Job.class, "com/appspot/piment/dao/resource/Job.json", "jobId");
	log.info("initJobStatus -- end");
  }

  @SuppressWarnings("unchecked")
  private <T> void restoreDataFromJsonFile(Class<T> entityClzss, String jsonFile, String... filterFields) {

	try {
	  pm = PMF.get().getPersistenceManager();

	  List<Map<String, String>> entityList = JSON.decode(this.getClass().getClassLoader().getResourceAsStream(jsonFile), List.class);

	  T entity = null;
	  Field field = null;
	  StringBuilder filterStr = null;
	  Map<String, Object> paramsMap = null;

	  for (Map<String, String> entry : entityList) {

		entity = JSON.decode(JSON.encode(entry), entityClzss);

		Query query = pm.newQuery(entityClzss);

		paramsMap = new LinkedHashMap<String, Object>();

		List<T> results = null;
		filterStr = new StringBuilder();
		String optStr = " && ";
		boolean checkUpdate = true;
		for (String fieldName : filterFields) {
		  filterStr.append(fieldName).append(" == :").append(fieldName);
		  filterStr.append(optStr);

		  field = entityClzss.getDeclaredField(fieldName);
		  field.setAccessible(true);

		  if (field.get(entity) == null) {
			checkUpdate = false;
			break;
		  }

		  paramsMap.put(fieldName, field.get(entity));

		}
		if (checkUpdate) {
		  filterStr = filterStr.delete(filterStr.length() - optStr.length(), filterStr.length() - 1);

		  query.setFilter(filterStr.toString());

		  results = (List<T>) query.executeWithMap(paramsMap);
		}

		if (results != null && results.size() > 0) {
		  pm.deletePersistent(results.get(0));
		  log.info("delete  --> " + entity);
		}
		
		log.info("creating --> " + entity);

		pm.makePersistent(entity);
	  }

	} catch (Exception e) {
	  e.printStackTrace();
	  throw new RuntimeException(e);
	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }

	}
  }
}
