package com.kenick.user.bean;

import java.util.ArrayList;

public class User{/* feild added*/
	private static final long serialVersionUID = 1L;

	public static final String S_userId = "user_id";
	public static final String S_name = "name";
	public static final String S_age = "age";

	public static final ArrayList<String> fieldList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("user_id");
			add("name");
			add("age");
		}	};


    /**
     * 
     */
    private String userId;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Integer age;

    /**
     * 
     * @return user_id 
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId 
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 
     * @return name 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 
     * @return age 
     */
    public Integer getAge() {
        return age;
    }

    /**
     * 
     * @param age 
     */
    public void setAge(Integer age) {
        this.age = age;
    }
}
