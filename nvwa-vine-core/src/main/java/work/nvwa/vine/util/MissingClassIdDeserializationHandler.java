package work.nvwa.vine.util;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

import java.io.IOException;


/**
 * @author Geng Rong
 */
public class MissingClassIdDeserializationHandler extends DeserializationProblemHandler {
    @Override
    public JavaType handleMissingTypeId(DeserializationContext ctxt, JavaType baseType, TypeIdResolver idResolver, String failureMsg) throws
            IOException {
        if (!baseType.isInterface() && !baseType.isAbstract()) {
            return baseType;
        }
        return super.handleMissingTypeId(ctxt, baseType, idResolver, failureMsg);
    }
}
