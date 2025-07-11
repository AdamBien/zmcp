package airhacks.zmcp.prompts.entity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import airhacks.zmcp.jsonrpc.entity.JsonRPCResponses;

public interface PromptResponses {

    static JSONObject listPrompts(int id, List<PromptSignature> prompts) {
        var promptJson = new JSONArray();
        prompts.forEach(prompt -> promptJson.put(prompt.toJson()));
        var jsonObject = JsonRPCResponses.response(id);
        var result = new JSONObject();
        result.put("prompts", promptJson);
        jsonObject.put("result", result);
        return jsonObject;
    }

    static JSONObject getPrompt(int id, PromptInstance prompt) {
        var jsonObject = JsonRPCResponses.response(id);
        var result = new JSONObject();
        var messages = new JSONArray();
        messages.put(prompt.message().toJson());
        result.put("description", prompt.description());
        result.put("messages", messages);
        jsonObject.put("result", result);
        return jsonObject;
    }
}
