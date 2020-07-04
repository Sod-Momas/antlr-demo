package calculator;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
//        runInTerminal();
        runFromString();
    }

    private static void runFromString() throws IOException {
        final String text
                = "1+2\n"
                + "3-4\n"
                + "5*6\n"
                + "8/4\n"
                + "a=9\n"
                + "b=10\n"
                + "a+b\n";
        batchCalcute(text);
    }

    private static void runInTerminal() throws IOException {
        final Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            final String line = scanner.nextLine();
            batchCalcute(line + '\n');
        }
        scanner.close();
    }

    private static void batchCalcute(String expr) throws IOException {
        final CharStream input = CharStreams.fromString(expr);
        final CalculatorLexer lexer = new CalculatorLexer(input);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final CalculatorParser parser = new CalculatorParser(tokens);
        final CalculatorParser.ProgContext context = parser.prog();

        final CalculatorVisitor visitor = new CalculatorVisitor();
        visitor.visit(context);
    }

    private static class CalculatorVisitor extends CalculatorBaseVisitor<Integer> {

        private final HashMap<String, Integer> memory = new HashMap<>();

        @Override
        public Integer visitPrintExpr(CalculatorParser.PrintExprContext ctx) {
            final Integer value = visit(ctx.expr());
            System.out.println(ctx.expr().getText() + '=' + value);
            return 0;
        }

        @Override
        public Integer visitAssign(CalculatorParser.AssignContext ctx) {
            final String id = ctx.ID().getText();
            final Integer value = visit(ctx.expr());
            memory.put(id, value);
            System.out.printf("SET %s=%d%n", id, value);
            return value;

        }

        @Override
        public Integer visitBlank(CalculatorParser.BlankContext ctx) {
            return super.visitBlank(ctx);
        }

        @Override
        public Integer visitParens(CalculatorParser.ParensContext ctx) {
            return visit(ctx.expr());
        }

        @Override
        public Integer visitMulDiv(CalculatorParser.MulDivContext ctx) {
            final Integer left = visit(ctx.expr(0));
            final Integer right = visit(ctx.expr(1));
            final int type = ctx.op.getType();
            return eval(left, type, right);
        }


        @Override
        public Integer visitAddSub(CalculatorParser.AddSubContext ctx) {
            final Integer left = visit(ctx.expr(0));
            final Integer right = visit(ctx.expr(1));
            final int type = ctx.op.getType();
            return eval(left, type, right);
        }

        @Override
        public Integer visitId(CalculatorParser.IdContext ctx) {
            String id = ctx.ID().getText();
            return Objects.requireNonNullElse(memory.get(id), 0);
        }

        @Override
        public Integer visitInt(CalculatorParser.IntContext ctx) {
//            return Integer.parseInt(ctx.children.get(0).getText());
            return Integer.parseInt(ctx.INT().getText());
        }

        /**
         * 计算表达式的值
         *
         * @param left  左值 left value
         * @param type  计算方式 the calculate method of expr
         * @param right 右值 right value
         * @return 计算出来的值
         */
        private Integer eval(Integer left, int type, Integer right) {
            switch (type) {
                case CalculatorParser.ADD:
                    return left + right;
                case CalculatorParser.SUB:
                    return left - right;
                case CalculatorParser.MUL:
                    return left * right;
                case CalculatorParser.DIV:
                    return left / right;
                default:
                    throw new RuntimeException("cannot oprerate type:" + type);
            }
        }
    }

}
