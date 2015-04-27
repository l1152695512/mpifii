package com.yinfu.common;
/**
 * 系统针对Ajax调用封装的结果集合
 * @author JiaYongChao
 *
 */
public class Result {
  
  public static final String SUCCESS = "success";
  public static final String ERROR = "error";
  public static final String SAVEERROR = "保存出错！";
  public static final String DELETEERROR = "删除过程中出错！";
  
  private String state = Result.SUCCESS; // 状态 success or error
  private String msg = "操作成功！"; // 提示信息
  private String toalert = "false";
  private String closewin = "true";
  
  public Result() {
     
  }
  
  public Result(String state, String msg) {
      super();
      this.state = state;
      this.msg = msg;
  }
  
  public Result(String state, String msg, String toalert, String closewin) {
      super();
      this.state = state;
      this.msg = msg;
      this.toalert = toalert;
      this.closewin = closewin;
  }
  
  public String getState() {
      return state;
  }
  
  public void setState(String state) {
      this.state = state;
  }
  
  public String getMsg() {
      return msg;
  }
  
  public void setMsg(String msg) {
      this.msg = msg;
  }
  
  public String getToalert() {
      return toalert;
  }
  
  public void setToalert(String toalert) {
      this.toalert = toalert;
  }
  
  public String getClosewin() {
      return closewin;
  }
  
  public void setClosewin(String closewin) {
      this.closewin = closewin;
  }
  
}
