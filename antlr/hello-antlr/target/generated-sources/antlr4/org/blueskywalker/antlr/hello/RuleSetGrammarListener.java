// Generated from org/blueskywalker/antlr/hello/RuleSetGrammar.g4 by ANTLR 4.3
package org.blueskywalker.antlr.hello;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RuleSetGrammarParser}.
 */
public interface RuleSetGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code LogicalVariable}
	 * labeled alternative in {@link RuleSetGrammarParser#logical_entity}.
	 * @param ctx the parse tree
	 */
	void enterLogicalVariable(@NotNull RuleSetGrammarParser.LogicalVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalVariable}
	 * labeled alternative in {@link RuleSetGrammarParser#logical_entity}.
	 * @param ctx the parse tree
	 */
	void exitLogicalVariable(@NotNull RuleSetGrammarParser.LogicalVariableContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ArithmeticExpressionMult}
	 * labeled alternative in {@link RuleSetGrammarParser#arithmetic_expr}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticExpressionMult(@NotNull RuleSetGrammarParser.ArithmeticExpressionMultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArithmeticExpressionMult}
	 * labeled alternative in {@link RuleSetGrammarParser#arithmetic_expr}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticExpressionMult(@NotNull RuleSetGrammarParser.ArithmeticExpressionMultContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ComparisonExpressionParens}
	 * labeled alternative in {@link RuleSetGrammarParser#comparison_expr}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpressionParens(@NotNull RuleSetGrammarParser.ComparisonExpressionParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ComparisonExpressionParens}
	 * labeled alternative in {@link RuleSetGrammarParser#comparison_expr}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpressionParens(@NotNull RuleSetGrammarParser.ComparisonExpressionParensContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ArithmeticExpressionMinus}
	 * labeled alternative in {@link RuleSetGrammarParser#arithmetic_expr}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticExpressionMinus(@NotNull RuleSetGrammarParser.ArithmeticExpressionMinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArithmeticExpressionMinus}
	 * labeled alternative in {@link RuleSetGrammarParser#arithmetic_expr}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticExpressionMinus(@NotNull RuleSetGrammarParser.ArithmeticExpressionMinusContext ctx);

	/**
	 * Enter a parse tree produced by the {@code NumericConst}
	 * labeled alternative in {@link RuleSetGrammarParser#numeric_entity}.
	 * @param ctx the parse tree
	 */
	void enterNumericConst(@NotNull RuleSetGrammarParser.NumericConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NumericConst}
	 * labeled alternative in {@link RuleSetGrammarParser#numeric_entity}.
	 * @param ctx the parse tree
	 */
	void exitNumericConst(@NotNull RuleSetGrammarParser.NumericConstContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ArithmeticExpressionParens}
	 * labeled alternative in {@link RuleSetGrammarParser#arithmetic_expr}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticExpressionParens(@NotNull RuleSetGrammarParser.ArithmeticExpressionParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArithmeticExpressionParens}
	 * labeled alternative in {@link RuleSetGrammarParser#arithmetic_expr}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticExpressionParens(@NotNull RuleSetGrammarParser.ArithmeticExpressionParensContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ArithmeticExpressionNumericEntity}
	 * labeled alternative in {@link RuleSetGrammarParser#arithmetic_expr}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticExpressionNumericEntity(@NotNull RuleSetGrammarParser.ArithmeticExpressionNumericEntityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArithmeticExpressionNumericEntity}
	 * labeled alternative in {@link RuleSetGrammarParser#arithmetic_expr}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticExpressionNumericEntity(@NotNull RuleSetGrammarParser.ArithmeticExpressionNumericEntityContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ArithmeticExpressionPlus}
	 * labeled alternative in {@link RuleSetGrammarParser#arithmetic_expr}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticExpressionPlus(@NotNull RuleSetGrammarParser.ArithmeticExpressionPlusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArithmeticExpressionPlus}
	 * labeled alternative in {@link RuleSetGrammarParser#arithmetic_expr}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticExpressionPlus(@NotNull RuleSetGrammarParser.ArithmeticExpressionPlusContext ctx);

	/**
	 * Enter a parse tree produced by the {@code NumericVariable}
	 * labeled alternative in {@link RuleSetGrammarParser#numeric_entity}.
	 * @param ctx the parse tree
	 */
	void enterNumericVariable(@NotNull RuleSetGrammarParser.NumericVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NumericVariable}
	 * labeled alternative in {@link RuleSetGrammarParser#numeric_entity}.
	 * @param ctx the parse tree
	 */
	void exitNumericVariable(@NotNull RuleSetGrammarParser.NumericVariableContext ctx);

	/**
	 * Enter a parse tree produced by {@link RuleSetGrammarParser#conclusion}.
	 * @param ctx the parse tree
	 */
	void enterConclusion(@NotNull RuleSetGrammarParser.ConclusionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleSetGrammarParser#conclusion}.
	 * @param ctx the parse tree
	 */
	void exitConclusion(@NotNull RuleSetGrammarParser.ConclusionContext ctx);

	/**
	 * Enter a parse tree produced by the {@code LogicalEntity}
	 * labeled alternative in {@link RuleSetGrammarParser#logical_expr}.
	 * @param ctx the parse tree
	 */
	void enterLogicalEntity(@NotNull RuleSetGrammarParser.LogicalEntityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalEntity}
	 * labeled alternative in {@link RuleSetGrammarParser#logical_expr}.
	 * @param ctx the parse tree
	 */
	void exitLogicalEntity(@NotNull RuleSetGrammarParser.LogicalEntityContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ComparisonExpression}
	 * labeled alternative in {@link RuleSetGrammarParser#logical_expr}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpression(@NotNull RuleSetGrammarParser.ComparisonExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ComparisonExpression}
	 * labeled alternative in {@link RuleSetGrammarParser#logical_expr}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpression(@NotNull RuleSetGrammarParser.ComparisonExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link RuleSetGrammarParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(@NotNull RuleSetGrammarParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleSetGrammarParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(@NotNull RuleSetGrammarParser.ConditionContext ctx);

	/**
	 * Enter a parse tree produced by the {@code LogicalExpressionInParen}
	 * labeled alternative in {@link RuleSetGrammarParser#logical_expr}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExpressionInParen(@NotNull RuleSetGrammarParser.LogicalExpressionInParenContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalExpressionInParen}
	 * labeled alternative in {@link RuleSetGrammarParser#logical_expr}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExpressionInParen(@NotNull RuleSetGrammarParser.LogicalExpressionInParenContext ctx);

	/**
	 * Enter a parse tree produced by {@link RuleSetGrammarParser#single_rule}.
	 * @param ctx the parse tree
	 */
	void enterSingle_rule(@NotNull RuleSetGrammarParser.Single_ruleContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleSetGrammarParser#single_rule}.
	 * @param ctx the parse tree
	 */
	void exitSingle_rule(@NotNull RuleSetGrammarParser.Single_ruleContext ctx);

	/**
	 * Enter a parse tree produced by {@link RuleSetGrammarParser#comp_operator}.
	 * @param ctx the parse tree
	 */
	void enterComp_operator(@NotNull RuleSetGrammarParser.Comp_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleSetGrammarParser#comp_operator}.
	 * @param ctx the parse tree
	 */
	void exitComp_operator(@NotNull RuleSetGrammarParser.Comp_operatorContext ctx);

	/**
	 * Enter a parse tree produced by {@link RuleSetGrammarParser#rule_set}.
	 * @param ctx the parse tree
	 */
	void enterRule_set(@NotNull RuleSetGrammarParser.Rule_setContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleSetGrammarParser#rule_set}.
	 * @param ctx the parse tree
	 */
	void exitRule_set(@NotNull RuleSetGrammarParser.Rule_setContext ctx);

	/**
	 * Enter a parse tree produced by the {@code LogicalExpressionAnd}
	 * labeled alternative in {@link RuleSetGrammarParser#logical_expr}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExpressionAnd(@NotNull RuleSetGrammarParser.LogicalExpressionAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalExpressionAnd}
	 * labeled alternative in {@link RuleSetGrammarParser#logical_expr}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExpressionAnd(@NotNull RuleSetGrammarParser.LogicalExpressionAndContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ComparisonExpressionWithOperator}
	 * labeled alternative in {@link RuleSetGrammarParser#comparison_expr}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpressionWithOperator(@NotNull RuleSetGrammarParser.ComparisonExpressionWithOperatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ComparisonExpressionWithOperator}
	 * labeled alternative in {@link RuleSetGrammarParser#comparison_expr}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpressionWithOperator(@NotNull RuleSetGrammarParser.ComparisonExpressionWithOperatorContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ArithmeticExpressionDiv}
	 * labeled alternative in {@link RuleSetGrammarParser#arithmetic_expr}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticExpressionDiv(@NotNull RuleSetGrammarParser.ArithmeticExpressionDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArithmeticExpressionDiv}
	 * labeled alternative in {@link RuleSetGrammarParser#arithmetic_expr}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticExpressionDiv(@NotNull RuleSetGrammarParser.ArithmeticExpressionDivContext ctx);

	/**
	 * Enter a parse tree produced by the {@code LogicalExpressionOr}
	 * labeled alternative in {@link RuleSetGrammarParser#logical_expr}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExpressionOr(@NotNull RuleSetGrammarParser.LogicalExpressionOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalExpressionOr}
	 * labeled alternative in {@link RuleSetGrammarParser#logical_expr}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExpressionOr(@NotNull RuleSetGrammarParser.LogicalExpressionOrContext ctx);

	/**
	 * Enter a parse tree produced by {@link RuleSetGrammarParser#comparison_operand}.
	 * @param ctx the parse tree
	 */
	void enterComparison_operand(@NotNull RuleSetGrammarParser.Comparison_operandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuleSetGrammarParser#comparison_operand}.
	 * @param ctx the parse tree
	 */
	void exitComparison_operand(@NotNull RuleSetGrammarParser.Comparison_operandContext ctx);

	/**
	 * Enter a parse tree produced by the {@code ArithmeticExpressionNegation}
	 * labeled alternative in {@link RuleSetGrammarParser#arithmetic_expr}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticExpressionNegation(@NotNull RuleSetGrammarParser.ArithmeticExpressionNegationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArithmeticExpressionNegation}
	 * labeled alternative in {@link RuleSetGrammarParser#arithmetic_expr}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticExpressionNegation(@NotNull RuleSetGrammarParser.ArithmeticExpressionNegationContext ctx);

	/**
	 * Enter a parse tree produced by the {@code LogicalConst}
	 * labeled alternative in {@link RuleSetGrammarParser#logical_entity}.
	 * @param ctx the parse tree
	 */
	void enterLogicalConst(@NotNull RuleSetGrammarParser.LogicalConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalConst}
	 * labeled alternative in {@link RuleSetGrammarParser#logical_entity}.
	 * @param ctx the parse tree
	 */
	void exitLogicalConst(@NotNull RuleSetGrammarParser.LogicalConstContext ctx);
}