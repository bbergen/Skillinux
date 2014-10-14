/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bryanbergen.Skillinux.Entities;

import junit.framework.TestCase;

/**
 *
 * @author bryan
 */
public class EveCharacterTest extends TestCase {
    
    public EveCharacterTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of equals method, of class EveCharacter.
     */
    public void testEquals() {
        System.out.println("equals");
        Object o = null;
        EveCharacter instance = new EveCharacter();
        DemoCharacter demo = new DemoCharacter();
        assertEquals(false, instance.equals(o));
        assertEquals(false, instance.equals(demo));
        assertEquals(true, demo.equals(new DemoCharacter()));
    }
}
