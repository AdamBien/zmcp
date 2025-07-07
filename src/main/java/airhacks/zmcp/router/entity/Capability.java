package airhacks.zmcp.router.entity;

import org.json.JSONObject;

public record Capability(String name, boolean listChanged) {
    
    public static Capability of(String name){
        return new Capability(name, false);
    }

    public static Capability of(String name, boolean listChanged){
        return new Capability(name, listChanged);
    }

    public  JSONObject toJSON(){
        var json = new JSONObject();
        var tools = new JSONObject();
        json.put(this.name, tools);
        tools.put("listChanged", this.listChanged);
        return json;
    }
}
