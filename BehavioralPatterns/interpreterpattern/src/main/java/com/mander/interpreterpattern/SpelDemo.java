package com.mander.interpreterpattern;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpelDemo {
    public static void main(String[] args){
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("2 + 3 * 4");
        Integer resultado = (Integer) exp.getValue();
        System.out.println("Resultado: " + resultado);
    }
}
