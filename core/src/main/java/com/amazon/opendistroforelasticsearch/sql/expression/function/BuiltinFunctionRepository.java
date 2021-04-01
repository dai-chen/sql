package com.amazon.opendistroforelasticsearch.sql.expression.function;

import static com.amazon.opendistroforelasticsearch.sql.expression.function.BuiltinFunctionName.CAST_TO_BOOLEAN;
import static com.amazon.opendistroforelasticsearch.sql.expression.function.BuiltinFunctionName.CAST_TO_DATE;
import static com.amazon.opendistroforelasticsearch.sql.expression.function.BuiltinFunctionName.CAST_TO_DOUBLE;
import static com.amazon.opendistroforelasticsearch.sql.expression.function.BuiltinFunctionName.CAST_TO_FLOAT;
import static com.amazon.opendistroforelasticsearch.sql.expression.function.BuiltinFunctionName.CAST_TO_INT;
import static com.amazon.opendistroforelasticsearch.sql.expression.function.BuiltinFunctionName.CAST_TO_LONG;
import static com.amazon.opendistroforelasticsearch.sql.expression.function.BuiltinFunctionName.CAST_TO_STRING;
import static com.amazon.opendistroforelasticsearch.sql.expression.function.BuiltinFunctionName.CAST_TO_TIME;
import static com.amazon.opendistroforelasticsearch.sql.expression.function.BuiltinFunctionName.CAST_TO_TIMESTAMP;

import com.amazon.opendistroforelasticsearch.sql.data.type.ExprCoreType;
import com.amazon.opendistroforelasticsearch.sql.data.type.ExprType;
import com.amazon.opendistroforelasticsearch.sql.exception.ExpressionEvaluationException;
import com.amazon.opendistroforelasticsearch.sql.expression.Expression;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Builtin Function Repository.
 */
@RequiredArgsConstructor
public class BuiltinFunctionRepository {
  private final Map<FunctionName, FunctionResolver> functionResolverMap;

  private final Set<FunctionName> castFunctions =
      ImmutableSet.<FunctionName>builder()
          .add(CAST_TO_STRING.getName())
          .add(CAST_TO_INT.getName())
          .add(CAST_TO_LONG.getName())
          .add(CAST_TO_FLOAT.getName())
          .add(CAST_TO_DOUBLE.getName())
          .add(CAST_TO_BOOLEAN.getName())
          .add(CAST_TO_DATE.getName())
          .add(CAST_TO_TIME.getName())
          .add(CAST_TO_TIMESTAMP.getName())
          .build();

  /**
   * Register {@link FunctionResolver} to the Builtin Function Repository.
   *
   * @param resolver {@link FunctionResolver} to be registered
   */
  public void register(FunctionResolver resolver) {
    functionResolverMap.put(resolver.getFunctionName(), resolver);
  }

  /**
   * Compile FunctionExpression.
   */
  public FunctionImplementation compile(FunctionName functionName, List<Expression> expressions) {
    FunctionBuilder resolvedFunctionBuilder = resolve(new FunctionSignature(functionName,
        expressions.stream().map(expression -> expression.type()).collect(Collectors.toList())));
    return resolvedFunctionBuilder.apply(expressions);
  }

  /**
   * Resolve the {@link FunctionBuilder} in Builtin Function Repository.
   *
   * @param functionSignature {@link FunctionSignature}
   * @return {@link FunctionBuilder}
   */
  public FunctionBuilder resolve(FunctionSignature functionSignature) {
    FunctionName functionName = functionSignature.getFunctionName();
    if (functionResolverMap.containsKey(functionName)) {
      Pair<FunctionSignature, FunctionBuilder> resolved =
          functionResolverMap.get(functionName).resolve(functionSignature);

      if (castFunctions.contains(functionName)) {
        return resolved.getValue();
      }
      return wrapArgsByCastFunction(functionSignature, resolved.getKey(), resolved.getValue());
    } else {
      throw new ExpressionEvaluationException(
          String.format("unsupported function name: %s", functionName.getFunctionName()));
    }
  }

  /**
   * Wrap resolved function builder's arguments by cast function if target type is different from
   * source type in unresolved signature. The cast function is to cast runtime expression value
   * to value of target type.
   * For example, suppose unresolved signature is =(BOOL,STRING), and resolved function builder is A
   * with signature =(BOOL,BOOL). In this case, wrap A to return =(BOOL, cast_to_bool(STRING))
   */
  private FunctionBuilder wrapArgsByCastFunction(FunctionSignature source,
                                                 FunctionSignature target,
                                                 FunctionBuilder funcBuilder) {
    List<ExprType> sourceTypes = source.getParamTypeList();
    List<ExprType> targetTypes = target.getParamTypeList();
    if (sourceTypes.equals(targetTypes)) {
      return funcBuilder;
    }

    return arguments -> {
      Set<ExprType> numTypes = new HashSet<>(ExprCoreType.numberTypes());
      List<Expression> argsCasted = new ArrayList<>();
      for (int i = 0; i < arguments.size(); i++) {
        Expression arg = arguments.get(i);
        ExprType sourceType = sourceTypes.get(i);
        ExprType targetType = targetTypes.get(i);

        if (sourceType.equals(targetType) || numTypes.contains(targetType)) {
          argsCasted.add(arg);
        } else {
          argsCasted.add(cast(arg, targetType));
        }
      }
      return funcBuilder.apply(argsCasted);
    };
  }

  private Expression cast(Expression arg, ExprType targetType) {
    FunctionName castFuncName = new FunctionName("cast_to_" + targetType.typeName().toLowerCase());
    return (Expression) compile(castFuncName, Arrays.asList(arg));
  }

}
