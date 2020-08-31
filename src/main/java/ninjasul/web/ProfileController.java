package ninjasul.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final Environment env;

    @GetMapping("/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real", "real1", "real2");

        return profiles.stream()
            .filter(realProfiles::contains)
            .findAny()
            .orElse(getDefaultProfile(profiles));
    }

    private String getDefaultProfile(List<String> profiles) {
        if (CollectionUtils.isEmpty(profiles)) {
            return "default";
        }

        return profiles.get(0);
    }
}
