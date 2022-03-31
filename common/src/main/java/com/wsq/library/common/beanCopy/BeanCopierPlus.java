package com.wsq.library.common.beanCopy;

import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

public abstract class BeanCopierPlus {
    private static final BeanCopierPlus.BeanCopierPlusKey KEY_FACTORY = (BeanCopierPlus.BeanCopierPlusKey) KeyFactory.create(BeanCopierPlus.BeanCopierPlusKey.class);
    private static final Type CONVERTER = TypeUtils.parseType(CopyConverter.class.getCanonicalName());
    private static final Type BEAN_COPIER = TypeUtils.parseType(BeanCopierPlus.class.getCanonicalName());
    private static final Signature COPY;
    private static final Signature CONVERT;

    public BeanCopierPlus() {
    }

    public static BeanCopierPlus create(Class source, Class target, boolean useConverter) {
        BeanCopierPlus.Generator gen = new BeanCopierPlus.Generator();
        gen.setSource(source);
        gen.setTarget(target);
        gen.setUseConverter(useConverter);
        return gen.create();
    }

    public abstract void copy(Object var1, Object var2, CopyConverter var3);

    static {
        // 分别是方法名、返回值，参数类型列表
        COPY = new Signature("copy", Type.VOID_TYPE, new Type[]{Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER});
        CONVERT = TypeUtils.parseSignature("Object convert(Object, Class, Object, Object)");
    }

    public static class Generator extends AbstractClassGenerator {
        private static final Source SOURCE = new Source(BeanCopierPlus.class.getName());
        private Class source;
        private Class target;
        private boolean useConverter;

        public Generator() {
            super(SOURCE);
        }

        public void setSource(Class source) {
            if (!Modifier.isPublic(source.getModifiers())) {
                this.setNamePrefix(source.getName());
            }

            this.source = source;
        }

        public void setTarget(Class target) {
            if (!Modifier.isPublic(target.getModifiers())) {
                this.setNamePrefix(target.getName());
            }

            this.target = target;
        }

        public void setUseConverter(boolean useConverter) {
            this.useConverter = useConverter;
        }

        protected ClassLoader getDefaultClassLoader() {
            return this.source.getClassLoader();
        }

        protected ProtectionDomain getProtectionDomain() {
            return ReflectUtils.getProtectionDomain(this.source);
        }

        public BeanCopierPlus create() {
            Object key = BeanCopierPlus.KEY_FACTORY.newInstance(this.source.getName(), this.target.getName(), this.useConverter);
            return (BeanCopierPlus) super.create(key);
        }

        /**
         * 假设字节码代码如下
         * UserDto var4 = (UserDto)var2;
         * User var5 = (User)var1;
         * Object var10001 = var3.convert(new Integer(var5.getA()), Integer.TYPE, "setA", new Integer(var4.getA()));
         * var4.setA(var10001 == null ? 0 : ((Number)var10001).intValue());
         * var4.setB((String)var3.convert(var5.getB(), CGLIB$load_class$java$2Elang$2EString, "setB", var4.getB()));
         *
         *user为source userdto为target
         */
        public void generateClass(ClassVisitor v) {
            Type sourceType = Type.getType(this.source);
            Type targetType = Type.getType(this.target);
            ClassEmitter ce = new ClassEmitter(v);
            // 获取类
            ce.begin_class(52, 1, this.getClassName(), BeanCopierPlus.BEAN_COPIER, (Type[]) null, "<generated>");
            EmitUtils.null_constructor(ce);
            // 获取copy方法
            CodeEmitter e = ce.begin_method(1, BeanCopierPlus.COPY, null);

            PropertyDescriptor[] sourceGetters = ReflectUtils.getBeanGetters(this.source);
            PropertyDescriptor[] targetSetters = ReflectUtils.getBeanSetters(this.target);
            PropertyDescriptor[] targetGetters = ReflectUtils.getBeanGetters(this.target);

            Map<String, PropertyDescriptor> sourceGetterMap = new HashMap<>();
            for (PropertyDescriptor descriptor : sourceGetters) {
                sourceGetterMap.put(descriptor.getName(), descriptor);
            }

            Map<String, PropertyDescriptor> targetGetterMap = new HashMap<>();
            for (PropertyDescriptor descriptor : targetGetters) {
                targetGetterMap.put(descriptor.getName(), descriptor);
            }

            Local targetLocal = e.make_local();
            Local sourceLocal = e.make_local();
            if (this.useConverter) {
                // 加载copy方法第二个参数，也就是targetObject，对应var2
                e.load_arg(1);
                // 校验，(UserDto)var2
                e.checkcast(targetType);
                // 对应UserDto var4 = (UserDto)var2;
                e.store_local(targetLocal);
                // 加载copy方法第一个参数，也就是sourceObject，对应var1
                e.load_arg(0);
                // 校验，对应(User)var1
                e.checkcast(sourceType);
                // 对应User var5 = (User)var1;
                e.store_local(sourceLocal);
            } else {
                e.load_arg(1);
                e.checkcast(targetType);
                e.load_arg(0);
                e.checkcast(sourceType);
            }

            for (PropertyDescriptor targetSetter : targetSetters) {
                PropertyDescriptor sourceGetter = sourceGetterMap.get(targetSetter.getName());
                PropertyDescriptor targetGetter = targetGetterMap.get(targetSetter.getName());

                if (sourceGetter != null) {
                    MethodInfo sourceRead = ReflectUtils.getMethodInfo(sourceGetter.getReadMethod());
                    MethodInfo targetRead = ReflectUtils.getMethodInfo(targetGetter.getReadMethod());
                    MethodInfo targetWrite = ReflectUtils.getMethodInfo(targetSetter.getWriteMethod());

                    // ps字节码变成没有花括号，所以有时候觉得加载有点怪
                    if (this.useConverter) {
                        // 获取目标字段类型
                        Type setterType = targetWrite.getSignature().getArgumentTypes()[0];
                        // 加载局部变量，对应var4
                        e.load_local(targetLocal);
                        // 加载copy方法第三个参数，也就是converter，对应var3
                        e.load_arg(2);
                        // 加载局部变量，对应var5
                        e.load_local(sourceLocal);
                        // 对应var5.getA()
                        e.invoke(sourceRead);
                        // 装箱，对应new Integer(var5.getA())
                        e.box(sourceRead.getSignature().getReturnType());
                        // 对应Integer.TYPE
                        EmitUtils.load_class(e, setterType);
                        // 对应"setA"
                        e.push(targetWrite.getSignature().getName());

                        // 加载局部变量，对应ar4
                        e.load_local(targetLocal);
                        // 对应var4.getA()
                        e.invoke(targetRead);
                        // 装箱，对应new Integer(var4.getA())
                        e.box(targetRead.getSignature().getReturnType());

                        // 执行converter方法
                        e.invoke_interface(BeanCopierPlus.CONVERTER, BeanCopierPlus.CONVERT);
                        // 拆箱及null赋0，对应var10001 == null ? 0 : ((Number)var10001).intValue()
                        e.unbox_or_zero(setterType);
                        // 执行target的set方法，对应var4.setA
                        e.invoke(targetWrite);
                    } else if (compatible(sourceGetter, targetSetter)) {
                        e.dup2();
                        e.invoke(sourceRead);
                        e.invoke(targetWrite);
                    }
                }
            }

            e.return_value();
            e.end_method();
            ce.end_class();
        }

        private static boolean compatible(PropertyDescriptor getter, PropertyDescriptor setter) {
            return setter.getPropertyType().isAssignableFrom(getter.getPropertyType());
        }

        protected Object firstInstance(Class type) {
            return ReflectUtils.newInstance(type);
        }

        protected Object nextInstance(Object instance) {
            return instance;
        }
    }

    interface BeanCopierPlusKey {
        Object newInstance(String var1, String var2, boolean var3);
    }
}
