package com.base.lib;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by biao.yin on 2018/5/4.
 *
 * @author yinbiao
 */

public class JsonUtil {
    private static Gson gson = new Gson();

    public static <T> String toJson(T t) {
        return gson.toJson(t);
    }

    /**
     * JsonUtil.fromJson(longinString, LoginInfo.class)
     *
     * @param json    json字符串
     * @param typeOfT 目标类型
     * @return T
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    /**
     * String json = "{\"code\":200,\"message\":\"login successful\",\"response\":{\"Jwt\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJMaW1zSWRlbnRpdHlTZXJ2aWNlIiwic3ViIjoiTElNUyIsImF1ZCI6ImxpbXMudG9ub2luZm8uY29tIiwiZXhwIjoxNTM0ODE0Nzc0LCJ1aWQiOjEwMDQxLCJvZ2MiOiJUTi0wMDAwMDAiLCJvZ24iOiJCaW9CYW5rIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvbmFtZSI6IuWUkOWNmiIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2VtYWlsYWRkcmVzcyI6ImNpZ2FyYm9AMTYzLmNvbSIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL21vYmlsZXBob25lIjoiMTg1NzY2ODY0NDEiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJVc2VyIn0.y4Tbh-AWEpEEV2zpBqzYtoxrZswDwovXrqahV2Qx5FQ\",\"Token\":\"DB4B64E0-45DF-4B2D-9101-4E1A4EDE2782\",\"Identity\":\"F8AA5C7D-89D5-4EAB-A05A-2AEC380E2132\",\"OrgCode\":\"TN-000000\",\"OrgName\":\"BioBank\",\"AccountInfo\":{\"ID\":10041,\"MobilePhone\":\"18576686441\",\"Email\":\"cigarbo@163.com\",\"Password\":\"/yGfcad1ihI=\",\"RealName\":\"唐博\",\"Photo\":\"default.png\",\"RegTime\":\"2018-03-07 10:31:13\",\"LastLoginTime\":\"2018-08-20 09:24:14\"}}}";
     * JsonResult<LoginInfo> result = JsonUtil.fromJson(json,JsonResult.class, LoginInfo.class);
     * String json = "{\"total\":2,\"pageNumber\":1,\"rows\":[{\"ID\":180,\"CreateTime\":\"2018-08-15 09:25:52\",\"OrderNumber\":\"A18081500180\",\"ProjectCode\":null,\"ProjectName\":null,\"ProjectId\":0,\"StudyCode\":null,\"StudyTitle\":null,\"StudyId\":0,\"SampleType\":null,\"State\":0,\"Remark\":null,\"Status\":0,\"OrganCode\":\"TN-000000\",\"CreateUsersId\":10047,\"CreateUsersName\":\"尹彪\",\"OutTime\":\"0001-01-01T00:00:00\",\"OutState\":null,\"OutUsersName\":null,\"ManagerName\":null,\"ManagerUsersId\":0,\"ApproveUsers\":\"\",\"ApproveStatus\":200,\"FlowNodeId\":-1,\"SamplesNum\":3},{\"ID\":161,\"CreateTime\":\"2018-08-13 16:20:09\",\"OrderNumber\":\"A18081300161\",\"ProjectCode\":null,\"ProjectName\":null,\"ProjectId\":0,\"StudyCode\":null,\"StudyTitle\":null,\"StudyId\":0,\"SampleType\":null,\"State\":0,\"Remark\":null,\"Status\":0,\"OrganCode\":\"TN-000000\",\"CreateUsersId\":10031,\"CreateUsersName\":\"吴亮\",\"OutTime\":\"0001-01-01T00:00:00\",\"OutState\":null,\"OutUsersName\":null,\"ManagerName\":null,\"ManagerUsersId\":0,\"ApproveUsers\":\"\",\"ApproveStatus\":200,\"FlowNodeId\":-1,\"SamplesNum\":2}],\"code\":200,\"message\":null,\"response\":null}";
     * JsonResult<SampleAcceptance> result = JsonUtil.fromJson(json,JsonResult.class, SampleAcceptance.class);
     * JsonResult<List<String>> result = JsonUtil.formJson(json,JsonrResult.class, new TypeToken<List<String>>(){}.getType() );
     *
     * @param json           json字符串
     * @param typeofT        结果集类型
     * @param typeOfArgument 结果集参数类型
     */
   public static <T> T fromJson(String json, final Type typeofT, final Type typeOfArgument) {
        try {
            //T中的结果集为Object类型
            return gson.fromJson(json, new ParameterizedTypeImpl(typeofT, typeOfArgument));
        } catch (Exception e) {
            //T中的结果集为List类型
            return gson.fromJson(json, new ParameterizedTypeImpl(typeofT, new ParameterizedTypeImpl(List.class, typeOfArgument)));
        }
    }

    static class ParameterizedTypeImpl implements ParameterizedType {

        private Type rawType;
        private Type actualTypeArguments;

        public ParameterizedTypeImpl(Type rawType, Type actualTypeArguments) {
            this.rawType = rawType;
            this.actualTypeArguments = actualTypeArguments;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{actualTypeArguments};
        }

        @Override
        public Type getRawType() {
            return rawType;
        }

        @Nullable
        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
