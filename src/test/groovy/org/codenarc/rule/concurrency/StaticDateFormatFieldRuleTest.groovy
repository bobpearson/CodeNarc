/*
 * Copyright 2010 the original author or authors.
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
package org.codenarc.rule.concurrency

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule

/**
 * Tests for StaticDateFormatFieldRule
 *
 * @author Hamlet D'Arcy
 * @author Chris Mair
 */
class StaticDateFormatFieldRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'StaticDateFormatField'
    }

    void testSuccessScenario() {
        final SOURCE = '''
          // these usages are OK
          class MyCorrectClass {
            private final DateFormat dateFormat1
            static ThreadLocal<DateFormat> dateFormat2
            static object = new Object()
            public static final VALUE = 1234
          }
        '''
        assertNoViolations(SOURCE)
    }

    void testStaticField() {
        final SOURCE = '''
              class MyClass {
                static DateFormat dateFormat
              }
        '''
        assertSingleViolation(SOURCE, 3, 'static DateFormat dateFormat', 'DateFormat instances are not thread safe. Wrap the DateFormat field dateFormat in a ThreadLocal or make it an instance field')
    }

    void testStaticFieldFullyQualifiedName() {
        final SOURCE = '''
              class MyClass {
                static java.text.DateFormat dateFormat
              }
        '''
        assertSingleViolation(SOURCE, 3, 'static java.text.DateFormat dateFormat', 'DateFormat instances are not thread safe. Wrap the DateFormat field dateFormat in a ThreadLocal or make it an instance field')
    }

    void testStaticUntypedField_InitializesValueToDateFormat() {
        final SOURCE = '''
              class MyClass {
                static final DATE1 = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE)
                static final def DATE2 = DateFormat.getDateInstance(DateFormat.LONG)
                static Object date3 = DateFormat.getDateInstance()

                static final DATETIME1 = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.FRANCE)
                static final def DATETIME2 = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT)
                static Object dateTime3 = DateFormat.getDateTimeInstance()

                static final TIME1 = DateFormat.getTimeInstance(DateFormat.LONG, Locale.FRANCE)
                static final def TIME2 = DateFormat.getTimeInstance(DateFormat.LONG)
                static final Object TIME3 = DateFormat.getTimeInstance()
              }
        '''
        assertViolations(SOURCE,
            [lineNumber:3, sourceLineText:'static final DATE1 = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE)', messageText:['DateFormat', 'DATE1']],
            [lineNumber:4, sourceLineText:'static final def DATE2 = DateFormat.getDateInstance(DateFormat.LONG)', messageText:['DateFormat', 'DATE2']],
            [lineNumber:5, sourceLineText:'static Object date3 = DateFormat.getDateInstance()', messageText:['DateFormat', 'date3']],

            [lineNumber:7, sourceLineText:'static final DATETIME1 = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.FRANCE)', messageText:['DateFormat', 'DATETIME1']],
            [lineNumber:8, sourceLineText:'static final def DATETIME2 = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT)', messageText:['DateFormat', 'DATETIME2']],
            [lineNumber:9, sourceLineText:'static Object dateTime3 = DateFormat.getDateTimeInstance()', messageText:['DateFormat', 'dateTime3']],

            [lineNumber:11, sourceLineText:'static final TIME1 = DateFormat.getTimeInstance(DateFormat.LONG, Locale.FRANCE)', messageText:['DateFormat', 'TIME1']],
            [lineNumber:12, sourceLineText:'static final def TIME2 = DateFormat.getTimeInstance(DateFormat.LONG)', messageText:['DateFormat', 'TIME2']],
            [lineNumber:13, sourceLineText:'static final Object TIME3 = DateFormat.getTimeInstance()', messageText:['DateFormat', 'TIME3']],
        )
    }

    protected Rule createRule() {
        new StaticDateFormatFieldRule()
    }
}