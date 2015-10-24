# Alex's Decaf Compiler
https://github.com/a1exwang/decaf
## Stage 1: Lexer and Parser

### Lexer

需要添加的token有以下, 由于JFlex从上到下依次匹配正则表达式并创建token, 所以应注意, 关键字需要写在identifier之前


    "++"				{ return operator(Parser.INC); 			}
    "--"				{ return operator(Parser.DEC);			}
    "|||"				{ return operator(Parser.GUARD_SEPERATOR); }
    "fi"				{ return keyword(Parser.FI);			}
    "do"				{ return keyword(Parser.DO);			}
    "od"				{ return keyword(Parser.OD);			}
    ...
同时在Parser中需要添加对应的token常量.

### Parser
解决移进冲突主要有两种方式. 一是通过添加语法规则, 二是使用BYACC提供的优先级.
通过阅读代码可以知道, 原来Decaf语言中除了Expr外均使用方法一解决冲突, Expr使用方法二解决
冲突.

所以虽然文档中要求了自增操作符等操作符只能和Identifier连用, 但是我们在这里对所有的操作符, 都使用Expr, 而不是Identifier,
这样就可以方便地用优先级来解决冲突, 然后再在以后的语义分析中, 解决非Identifier的Expr和操作符连用的问题.

对Parser的修改如下:

1. 添加了一下的语法规则


    Expr:...
    ...
       |    INC Expr
            {
                $$.expr = new Tree.Unary(Tree.PREINC, $2.expr, $1.loc);
            }
        |   DEC Expr
        ...
        |   NUMINSTANCES '(' IDENTIFIER ')'
            {
                $$.expr = new Tree.NumInstances($3.ident, $1.loc);
            }
    ...
    Stmt:
        ...
        |   GuardStmt // 这里GuradStmt既可以是if又可以是while, 这两种共享同一种树节点, 用type来区分
    ...
    // 在这里实现了两种GuardStmt
    GuardStmt :         IF GuardClauses FI
                        ...
                    |   DO GuardClauses OD
                        ...
    // 考虑到两种GuardStmt除了关键字外, 结构是完全相同的, 所以这里统一为GuardClauses
    GuardClauses :      GuardClauses GUARD_SEPERATOR Expr ':' Stmt
                        ...
                    |   Expr ':' Stmt
                        ...

2. 同时在Tree中添加了对应的语法分析树节点类型.


    // 在Unary中加入了++和--操作符

    // numinstances
    public static class NumInstances extends Expr { ... }

    // 三元操作符, 用来表示 : ?
    public static class Ternary extends Expr {
        public Expr left;
        public Expr middle;
        public Expr right;
        ...
    }

    // Guard语句
    public static class GuardClause extends Tree { ... }
    public static class Guard extends Tree {
        public enum Type {
            Condition,
            Loop
        } // 用来区分Guard语句的类型
        Type type;
        List<GuardClause> clauses; // Guard内部的各个条件语句
        ...
    }

3. 下一步备注
    1. 检测Expr和Identifier语法正确, 语义不正确的情况
