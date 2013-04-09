package nz.net.initial3d.renderer;

import static nz.net.initial3d.renderer.Util.*;
import static nz.net.initial3d.renderer.Type.*;
import sun.misc.Unsafe;

public class Test {

	public static void main(String[] args) {
		
		Unsafe unsafe = getUnsafe();
		
		long pLight = unsafe.allocateMemory(1024);
		
		puts(pLight);
		
		puts(light_t.SIZEOF());
		
		puts(unsafe.getFloat(pLight + light_t.constant_attenuation()));
		
	}

}
