package com.cisco.csc.genericLib;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.xml.XmlMethodSelector;

public class MethodOrderExecutor implements IMethodInterceptor {

	@Override
	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
		// TODO Auto-generated method stub

		Comparator<IMethodInstance> comparator = new Comparator<IMethodInstance>() {
			private int getPriority(IMethodInstance mi) {
				int result = 0;
				Method method = mi.getMethod().getMethod();
				Priority a1 = method.getAnnotation(Priority.class);
				if (a1 != null) {
					result = a1.value();
				} else {
					Class<?> cls = method.getDeclaringClass();
					Priority classPriority = cls.getAnnotation(Priority.class);
					if (classPriority != null) {
						result = classPriority.value();
					}
				}
				return result;
			}

			public int compare(IMethodInstance m1, IMethodInstance m2) {
				return getPriority(m1) - getPriority(m2);
			}
		};

		IMethodInstance[] array = methods.toArray(new IMethodInstance[methods.size()]);
		Arrays.sort(array, comparator);
		List<IMethodInstance> methodsSortedByPriority = Arrays.asList(array);
		for (IMethodInstance iMethodInstance : methodsSortedByPriority) {
			System.out.println(iMethodInstance.getMethod().getMethodName());
		}
		
		return methodsSortedByPriority;

	}

}
