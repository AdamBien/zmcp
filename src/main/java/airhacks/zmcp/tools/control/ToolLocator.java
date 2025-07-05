package airhacks.zmcp.tools.control;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.function.Function;

public interface ToolLocator {

    static Optional<ToolInstance> findTool(String name) {
        return ServiceLoader.load(Function.class)
                .stream()
                .map(Provider::get)
                .map(ToolInstance::of)
                .flatMap(Optional::stream)
                .filter(ti -> hasName(ti, name))
                .findFirst();
    }

    static boolean hasName(ToolInstance toolInstance, String name) {
        return toolInstance.hasName(name);
    }

    static List<ToolInstance> all() {
        return ServiceLoader.load(Function.class)
                .stream()
                .map(Provider::get)
                .map(ToolInstance::of)
                .flatMap(Optional::stream)
                .toList();

    }

}
