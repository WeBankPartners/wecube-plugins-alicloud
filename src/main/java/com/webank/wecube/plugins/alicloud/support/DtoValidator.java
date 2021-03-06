package com.webank.wecube.plugins.alicloud.support;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author howechen
 */
@Component
public class DtoValidator {

    private static final Logger logger = LoggerFactory.getLogger(DtoValidator.class);
    private final Validator validator;

    public DtoValidator() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public <T extends CoreRequestInputDto> void validate(T o) throws PluginException {
        logger.info("Validating input DTO: [{}]", o.getClass().getSimpleName());
        final Set<ConstraintViolation<T>> validations = this.validator.validate(o);
        final List<String> validationStr = validations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        if (!validationStr.isEmpty()) {
            final String errorMsg = PluginStringUtils.stringifyObjectList(validationStr);
            logger.error(errorMsg);
            throw new PluginException(errorMsg);
        }
    }
}
