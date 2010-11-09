/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenarc.rule.basic

import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule

/**
 * In Groovy, the return keyword is often optional. If a statement is the last line in a method or closure then you do not need to have the return keyword. 
 *
 * @author 'Hamlet D'Arcy'
 * @version $Revision: 24 $ - $Date: 2009-01-31 13:47:09 +0100 (Sat, 31 Jan 2009) $
 */
class UnnecessaryReturnKeywordRule extends AbstractAstVisitorRule {
    String name = 'UnnecessaryReturnKeyword'
    int priority = 2
    Class astVisitorClass = UnnecessaryReturnKeywordAstVisitor
}

class UnnecessaryReturnKeywordAstVisitor extends AbstractAstVisitor {

    def void visitMethodEx(MethodNode node) {

        def lastStatement = getLastStatement(node)
        if (lastStatement instanceof ReturnStatement) {
            addViolation lastStatement
        }

        super.visitMethodEx node
    }

    def void visitClosureExpression(ClosureExpression node) {

        def lastStatement = getLastStatement(node)
        if (lastStatement instanceof ReturnStatement) {
            addViolation lastStatement
        }

        super.visitClosureExpression node
    }

    /**
     * This is not a good general function for AstUtils.
     * It is too specific and may not work across different ASTNode subtypes. 
     * @param node
     *      node
     */
    private static getLastStatement(node) {
        if (node.code instanceof BlockStatement && node.code.statements) {
            return node.code.statements.last()
        }
        null
    }
}