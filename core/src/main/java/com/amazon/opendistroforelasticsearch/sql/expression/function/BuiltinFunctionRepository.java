package com.amazon.opendistroforelasticsearch.sql.expression.function;

import static com.amazon.opendistroforelasticsearch.sql.ast.expression.Cast.getCastFunctionName;
import static com.amazon.opendistroforelasticsearch.sql.ast.expression.Cast.isCastFunction;

import com.amazon.opendistroforelasticsearch.sql.data.type.ExprCoreType;
import com.amazon.opendistroforelasticsearch.sql.data.type.ExprType;
import com.amazon.opendistroforelasticsearch.sql.exception.ExpressionEvaluationException;
import com.amazon.opendistroforelasticsearch.sql.expression.Expression;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Builtin Function Repository.
 */
@RequiredArgsConstructor
public class BuiltinFunctionRepository {
  private final Map<FunctionName, FunctionResolver> functionResolverMap;

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
      Pair<FunctionSignature, FunctionBuilder> resolvedSignature =
          functionResolverMap.get(functionName).resolve(functionSignature);

      List<ExprType> sourceTypes = functionSignature.getParamTypeList();
      List<ExprType> targetTypes = resolvedSignature.getKey().getParamTypeList();
      FunctionBuilder funcBuilder = resolvedSignature.getValue();
      if (isCastFunction(functionName) || sourceTypes.equals(targetTypes)) {
        return funcBuilder;
      }
      return castArguments(sourceTypes, targetTypes, funcBuilder);
    } else {
      throw new ExpressionEvaluationException(
          String.format("unsupported function name: %s", functionName.getFunctionName()));
    }
  }

  /**
   * Wrap resolved function builder's arguments by cast function if target type is different from
   * source type in unresolved signature. The cast function is to cast runtime expression value
   * to value of target type.
   * For example, suppose unresolved signature is equal(BOOL,STRING), and resolved function builder
   * is A with signature equal(BOOL,BOOL). In this case, wrap A to return
   * equal(BOOL, cast_to_bool(STRING))
   */
  private FunctionBuilder castArguments(List<ExprType> sourceTypes,
                                        List<ExprType> targetTypes,
                                        FunctionBuilder funcBuilder) {
    return arguments -> {
      List<Expression> argsCasted = new ArrayList<>();
      for (int i = 0; i < arguments.size(); i++) {
        Expression arg = arguments.get(i);
        ExprType sourceType = sourceTypes.get(i);
        ExprType targetType = targetTypes.get(i);

        if (isSameOrNumericalType(sourceType, targetType)) {
          argsCasted.add(arg);
        } else {
          argsCasted.add(cast(arg, targetType));
        }
      }
      return funcBuilder.apply(argsCasted);
    };
  }

  private boolean isSameOrNumericalType(ExprType sourceType, ExprType targetType) {
    if (sourceType.equals(targetType)) {
      return true;
    }
    // Casting from number to another number is built-in supported in JDK
    return ExprCoreType.numberTypes().contains(sourceType)
        && ExprCoreType.numberTypes().contains(targetType);
  }

  private Expression cast(Expression arg, ExprType targetType) {
    return (Expression) compile(getCastFunctionName(targetType), ImmutableList.of(arg));
  }

}
