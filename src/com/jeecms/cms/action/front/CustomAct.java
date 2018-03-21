package com.jeecms.cms.action.front;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.dao.main.ContentDao;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.entity.main.ContentExt;
import com.jeecms.cms.entity.main.ContentTxt;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CustomAct {
	public static final String COMMENT_PAGE = "tpl.commentPage";
	public static final String COMMENT_LIST = "tpl.commentList";
	public static final String COMMENT_INPUT = "tpl.commentInput";

	@RequestMapping(value = "*/iWannaJoin.jspx")
	public void iWannaJoin(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		JSONObject json = new JSONObject();
		String succ = "false";
		String msg = "";
		Integer vTotalNum = 0;
		String contentId = request.getParameter("contentId");
		CmsUser user = CmsUtils.getUser(request);
		try {
			if (user == null) {
				throw new Exception("请先登录");
			}
			String userId = user.getId().toString();
			if (contentId == null) {
				throw new Exception("传输数据有误");
			}
			Content content=contentMng.findById(Integer.parseInt(contentId));
			if(content==null)throw new Exception("找不到内容，可能已丢失");
			String volunteers = content.getAttr("volunteers");
			String maxcount = content.getAttr("maxcount");
			if(StringUtils.isNotBlank(volunteers)){
				String[] vv = volunteers.split(",");
				vTotalNum = vv.length;
				if(Arrays.asList(vv).contains(userId)){
					throw new Exception("您已报名，不能重复提交");
				}else if(StringUtils.isNumeric(maxcount) && Integer.valueOf(maxcount).intValue() <= vTotalNum.intValue()){
					throw new Exception("已经超过报名上限，请留意下次活动！！");
				}else{
					volunteers+=","+userId;
				}
			}else{
				volunteers=userId;
			}
			Map<String,String>newAttrs = content.getAttr();
			
            newAttrs.put("volunteers", volunteers);
            content.setAttr(newAttrs);
            contentMng.update(content);
            vTotalNum++;
            succ="true";
//            contentMng.save(content, content.getContentExt(), content.getContentTxt(), content.getChannel().getId(), content.getType().getId(), false, user, true);
		} catch (Exception e) {
			msg=e.getMessage();
		} finally {
			json.put("succ", succ);
			json.put("msg", msg);
			json.put("vTotalNum", vTotalNum);
			ResponseUtils.renderJson(response, json.toString());
		}
	}

	@Autowired
	private ContentMng contentMng;
	@Autowired
	private ContentDao dao;
}
