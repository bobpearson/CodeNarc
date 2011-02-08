/*
 * Copyright 2011 the original author or authors.
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
package org.codenarc.rule.unnecessary

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule

/**
 * The 'public' modifier is not required on methods or classes. 
 *
 * @author Hamlet D'Arcy
 * @version $Revision: 24 $ - $Date: 2009-01-31 13:47:09 +0100 (Sat, 31 Jan 2009) $
 */
class UnnecessaryPublicModifierRule extends AbstractAstVisitorRule {
    String name = 'UnnecessaryPublicModifier'
    int priority = 2
    Class astVisitorClass = UnnecessaryPublicModifierAstVisitor
}

class UnnecessaryPublicModifierAstVisitor extends AbstractAstVisitor {
    @Override
    protected void visitClassEx(ClassNode node) {
        if (getDeclaration(node).contains('public')) {
            addViolation(node, 'The public keyword is unnecessary for classes')
        }
        super.visitClassEx(node)
    }

    @Override
    void visitMethodEx(MethodNode node) {
        if (getDeclaration(node).contains('public')) {
            addViolation(node, 'The public keyword is unnecessary for methods')
        }
        super.visitMethodEx(node)
    }



    private String getDeclaration(ASTNode node) {
        def current = node.lineNumber - 1
        String acc = ''
        while (current <= node.lastLineNumber && !acc.contains('{')) {
            acc = acc + sourceCode.line(current)
            current++
        }
        acc
    }
}