package test.home.jfinal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import test.db.helper.MySQLHelper;

import com.jfinal.core.Controller;
import com.jfinal.log.Log;

public class SheetController extends Controller {
	Log log = Log.getLog(SheetController.class);
	
	public void index(){
		SqlSession session = MySQLHelper.getSqlSessionFactory().openSession();
		session.select("test.db.mapping.SheetMapper.getAllAproveSheetByUserName", null);
		render("index.html");
	}
	
	public void login(){
		String userName = getPara("user.userName");
		String password = getPara("user.password");
		if(userName==null || password==null){
			
		}
		Map<String,String> param = new HashMap<String,String>();
		param.put("userName", userName);
		param.put("password", password);
		SqlSession session = MySQLHelper.getSqlSessionFactory().openSession();
		Map<String,String> user = session.selectOne("test.db.mapping.SheetMapper.getAllAproveSheetByUserName", param);
		session.close();
		if(user==null){
			
		}
		setAttr("user", user);
		setSessionAttr("user", user);
		
		toBeApproveSheets();
		
		render("/static/htmls/main.html");
	}
	
	public void toBeApproveSheets(){
		SqlSession session = MySQLHelper.getSqlSessionFactory().openSession();
		Map<String, String> user = getSessionAttr("user");
		List<Map<String,String>> sheets = session.selectList("test.db.mapping.SheetMapper.querySheetByCurrentApprover", user.get("name"));
		session.close();
		setAttr("sheets", sheets);
//		render("/static/htmls/mySheet.html");
	}
	public void getSheetDetail(){
		SqlSession session = MySQLHelper.getSqlSessionFactory().openSession();
		Integer id = new Integer(getParaToInt("id"));
		Map<String,String> sheet = session.selectOne("test.db.mapping.SheetMapper.querySheetBySheetId", id);
		session.close();
		setAttr("sheet", sheet);
		render("/static/htmls/mySheetDetail.html");
	}
	
	public void mySheetDetail(){
		Integer id = getParaToInt("id");
		SqlSession session = MySQLHelper.getSqlSessionFactory().openSession();
		Map<String,String> sheet = session.selectOne("test.db.mapping.SheetMapper.querySheetBySheetId", id);
		List<Map<String,Object>> recordList = session.selectList("test.db.mapping.SheetMapper.queryRecordBySheetId", id);
		session.close();
		setAttr("sheet", sheet);
		setAttr("recordList", recordList);
		render("/static/htmls/mySheetDetail.html");
	}
	
	
	public void gotoCreateSheetPre(){
		render("/static/htmls/createSheet.html");
	}
	public void getMyAppoveSheets(){
		SqlSession session = MySQLHelper.getSqlSessionFactory().openSession();
		Map<String, String> user = getSessionAttr("user");
		List<Map<String,String>> sheets = session.selectList("test.db.mapping.SheetMapper.querySheetByUserName", user.get("name"));
		session.close();
		setAttr("sheets", sheets);
		render("/static/htmls/mySheet.html");
	}
	public void approveSheet(){
		Map<String, String> user = getSessionAttr("user");
		String myName = user.get("name");
		Integer operate = getParaToInt("operate");
		String id = getPara("id");
		String opinion = getPara("opinion");
		String approvers = getPara("approvers");
		Integer sheetStatus = 1;//1表示正常审批，中间节点，未结束
		String currentApprover = "";
		if(operate==0){//拒绝，审批流程结束
			sheetStatus = 9;
		}else{
			String[] approverArr = approvers.split(",");
			for(int i=0; i<approverArr.length; i++){
				if(myName.equals(approverArr[i])){
					if(i==approverArr.length-1){//当前最后一位审批人
						sheetStatus = 9;
						currentApprover = myName;
					}else{
						sheetStatus = 1;
						currentApprover = approverArr[i+1];
					}
					break ;
				}
			}
		}
		
		Map<String,Object> para = new HashMap<String,Object>();
		para.put("approve_sheet_id", id);
		para.put("approver", user.get("name"));
		para.put("status", sheetStatus);
		para.put("approval_opinion", opinion);
		para.put("currentApprover", currentApprover);
		
		SqlSession session = MySQLHelper.getSqlSessionFactory().openSession(false);
		
			try {
				session.update("test.db.mapping.SheetMapper.updateSheetProcess", para);
				session.insert("test.db.mapping.SheetMapper.addApproveRecord", para);
				session.commit();
			} catch (Exception e) {
				session.rollback();
				log.error("更新审批单状态失败！", e);
			}finally{
				session.close();
			}
		session = MySQLHelper.getSqlSessionFactory().openSession();
		List<Map<String,String>> sheets = session.selectList("test.db.mapping.SheetMapper.querySheetByUserName", user.get("name"));
		session.close();
		setAttr("sheets", sheets);
		render("/static/htmls/mySheet.html");
	}
	
	public void createSheet(){
		String applicant = getPara("applicant");
		String remark = getPara("remark");
		String descript = getPara("descript");
		String[] approvers = getParaValues("approvers");
		StringBuilder sb = new StringBuilder();
		for(String approver : approvers){
			sb.append(approver).append(",");
		}
		Map<String,Object> sheet = new HashMap<String,Object>();
		sheet.put("applicant", applicant);
		sheet.put("descript", descript);
		sheet.put("version", 0);
		sheet.put("status", 1);
		sheet.put("nextIndex", 0);
		sheet.put("approvers", sb.toString());
		sheet.put("currentApprover", approvers[0]);
		sheet.put("nextIndex", 0);
		sheet.put("remark", remark);
		SqlSession session = MySQLHelper.getSqlSessionFactory().openSession();
		session.insert("test.db.mapping.SheetMapper.saveSheet", sheet);
		session.close();
		getMyAppoveSheets();
	}
}
