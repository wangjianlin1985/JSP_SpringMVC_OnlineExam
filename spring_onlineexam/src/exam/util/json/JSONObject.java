// 
// 
// 

package exam.util.json;

import exam.util.DataUtil;

public class JSONObject extends JSON
{
    @Override
    public JSONObject addElement(final String key, final String value) {
        if (DataUtil.isValid(key) && value != null) {
            this.json.append("\"").append(key).append("\":\"").append(value).append("\",");
        }
        return this;
    }
    
    @Override
    public JSONObject addElement(final String key, final JSON object) {
        if (DataUtil.isValid(key) && object != null) {
            this.json.append("\"").append(key).append("\":").append(object).append(",");
        }
        return this;
    }
    
    @Override
    protected char getBrace() {
        return '{';
    }
}
