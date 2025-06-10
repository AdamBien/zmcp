package airhacks.zmcp.tools.control;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import airhacks.zmcp.tools.api.ToolInvocation;

public interface ToolLocator {

    static Optional<ToolInstance> findTool(String name) {
        return ServiceLoader.load(ToolInvocation.class)
                .stream()
                .map(Provider::get)
                .map(ToolInstance::of)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();

    }

    static List<ToolInstance> all() {
        return ServiceLoader.load(ToolInvocation.class)
                .stream()
                .map(Provider::get)
                .map(ToolInstance::of)
                .filter(Optional::isPresent)
                .map(Optional::get)
               .toList();

    }

}
 