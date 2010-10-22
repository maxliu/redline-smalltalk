package st.redline.compiler;

import org.objectweb.asm.*;

import java.util.List;

public class JavaBytecodeEncoder extends ClassLoader implements Opcodes {

	public static final String DEFAULT_FILE_PACKAGE = "st/redline/";
	public static final String DEFAULT_JAVA_PACKAGE = "st.redline.";
	private static final String[] METHOD_SIGNATURE = {
		"()Lst/redline/ProtoObject;",
		"(Lst/redline/ProtoObject;)Lst/redline/ProtoObject;",
		"(Lst/redline/ProtoObject;Lst/redline/ProtoObject;)Lst/redline/ProtoObject;",
		"(Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;)Lst/redline/ProtoObject;",
		"(Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;)Lst/redline/ProtoObject;",
		"(Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;)Lst/redline/ProtoObject;",
		"(Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;)Lst/redline/ProtoObject;",
		"(Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;)Lst/redline/ProtoObject;"
	};

	private static final String[] SEND_SIGNATURE = {
		"(Ljava/lang/String;)Lst/redline/ProtoObject;",
		"(Ljava/lang/String;Lst/redline/ProtoObject;)Lst/redline/ProtoObject;",
		"(Ljava/lang/String;Lst/redline/ProtoObject;Lst/redline/ProtoObject;)Lst/redline/ProtoObject;",
		"(Ljava/lang/String;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;)Lst/redline/ProtoObject;",
		"(Ljava/lang/String;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;)Lst/redline/ProtoObject;",
		"(Ljava/lang/String;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;)Lst/redline/ProtoObject;",
		"(Ljava/lang/String;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;)Lst/redline/ProtoObject;",
		"(Ljava/lang/String;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;)Lst/redline/ProtoObject;",
		"(Ljava/lang/String;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;Lst/redline/ProtoObject;)Lst/redline/ProtoObject;"
	};

	private final ClassWriter classWriter;
	private final ClassWriter classClassWriter;
	private String subclass;
	private String qualifiedSubclass;
	private String classQualifiedSubclass;
	private String qualifiedSuperclass;
	private String classQualifiedSuperclass;
	private String sourcePath;

	public JavaBytecodeEncoder() {
		classWriter = new TracingClassWriter(ClassWriter.COMPUTE_MAXS);
		classClassWriter = new TracingClassWriter(ClassWriter.COMPUTE_MAXS);
	}

	public void defineClass(ClassDefinition classDefinition, String sourcePath) {
		this.subclass = classDefinition.subclass();
		this.sourcePath = sourcePath;
		qualifiedSubclass = DEFAULT_FILE_PACKAGE + subclass;
		classQualifiedSubclass = DEFAULT_FILE_PACKAGE + subclass + "$mClass";
		qualifiedSuperclass = DEFAULT_FILE_PACKAGE + classDefinition.superclass();
		if (subclass.equals("Object"))
			classQualifiedSuperclass = DEFAULT_FILE_PACKAGE + "Class";
		else
			classQualifiedSuperclass = DEFAULT_FILE_PACKAGE + classDefinition.superclass() + "$mClass";
		defineClass(sourcePath);
	}

	private void defineClass(String sourcePath) {
		defineClass(sourcePath, classWriter, qualifiedSubclass, qualifiedSuperclass);
		defineInstanceFields(classWriter, qualifiedSubclass, qualifiedSuperclass);
		defineInstanceInitialization(classWriter, qualifiedSubclass, qualifiedSuperclass);
		defineInstanceHelpers(classWriter, qualifiedSubclass, qualifiedSuperclass);

		defineClass(sourcePath, classClassWriter, classQualifiedSubclass, classQualifiedSuperclass);
		defineClassFields(classClassWriter, classQualifiedSubclass, classQualifiedSuperclass);
		defineClassInitialization(classClassWriter, classQualifiedSubclass, classQualifiedSuperclass, qualifiedSubclass);
		defineClassHelpers(classClassWriter, classQualifiedSubclass, classQualifiedSuperclass);

		defineClassRegistration(classWriter, qualifiedSubclass, qualifiedSuperclass);
	}

	private void defineClassHelpers(ClassWriter classWriter, String qualifiedSubclass, String qualifiedSuperclass) {
		MethodVisitor mv = classWriter.visitMethod(ACC_PROTECTED, "findMethod", "(Ljava/lang/String;)Ljava/lang/reflect/Method;", null, null);
		mv.visitCode();
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(19, l0);
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, classQualifiedSubclass, "_class_", "Lst/redline/Metaclass;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V");
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitLineNumber(20, l1);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, classQualifiedSubclass, "_class_", "Lst/redline/Metaclass;");
		mv.visitFieldInsn(GETFIELD, "st/redline/Metaclass", "_methods_", "Ljava/util/Map;");
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
		mv.visitTypeInsn(CHECKCAST, "java/lang/reflect/Method");
		mv.visitVarInsn(ASTORE, 2);
		Label l2 = new Label();
		mv.visitLabel(l2);
		mv.visitLineNumber(21, l2);
		mv.visitVarInsn(ALOAD, 2);
		Label l3 = new Label();
		mv.visitJumpInsn(IFNULL, l3);
		Label l4 = new Label();
		mv.visitLabel(l4);
		mv.visitLineNumber(22, l4);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitInsn(ARETURN);
		mv.visitLabel(l3);
		mv.visitLineNumber(23, l3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKESPECIAL, qualifiedSuperclass, "findMethod", "(Ljava/lang/String;)Ljava/lang/reflect/Method;");
		mv.visitInsn(ARETURN);
		Label l5 = new Label();
		mv.visitLabel(l5);
		mv.visitMaxs(2, 3);
		mv.visitEnd();
	}

	private void defineInstanceHelpers(ClassWriter classWriter, String qualifiedSubclass, String qualifiedSuperclass) {
		MethodVisitor mv = classWriter.visitMethod(ACC_PROTECTED, "findMethod", "(Ljava/lang/String;)Ljava/lang/reflect/Method;", null, null);
		mv.visitCode();
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(28, l0);
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, qualifiedSubclass, "_class_", "Lst/redline/Object$mClass;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V");
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitLineNumber(29, l1);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, qualifiedSubclass, "_class_", "L"+classQualifiedSubclass+";");
		mv.visitFieldInsn(GETFIELD, classQualifiedSubclass, "_methods_", "Ljava/util/Map;");
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
		mv.visitTypeInsn(CHECKCAST, "java/lang/reflect/Method");
		mv.visitVarInsn(ASTORE, 2);
		Label l2 = new Label();
		mv.visitLabel(l2);
		mv.visitLineNumber(30, l2);
		mv.visitVarInsn(ALOAD, 2);
		Label l3 = new Label();
		mv.visitJumpInsn(IFNULL, l3);
		Label l4 = new Label();
		mv.visitLabel(l4);
		mv.visitLineNumber(31, l4);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitInsn(ARETURN);
		mv.visitLabel(l3);
		mv.visitLineNumber(32, l3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKESPECIAL, qualifiedSuperclass, "findMethod", "(Ljava/lang/String;)Ljava/lang/reflect/Method;");
		mv.visitInsn(ARETURN);
		Label l5 = new Label();
		mv.visitLabel(l5);
		mv.visitLocalVariable("this", "L"+qualifiedSubclass+";", null, l0, l5, 0);
		mv.visitLocalVariable("selector", "Ljava/lang/String;", null, l0, l5, 1);
		mv.visitLocalVariable("method", "Ljava/lang/reflect/Method;", null, l2, l5, 2);
		mv.visitMaxs(2, 3);
		mv.visitEnd();
	}

	private void defineClassRegistration(ClassWriter classWriter, String qualifiedSubclass, String qualifiedSuperclass) {
		MethodVisitor mv = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
		mv.visitCode();
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(11, l0);
		mv.visitLdcInsn(subclass);
		mv.visitTypeInsn(NEW, classQualifiedSubclass);
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, classQualifiedSubclass, "<init>", "()V");
		mv.visitMethodInsn(INVOKESTATIC, "st/redline/Smalltalk", "register", "(Ljava/lang/String;Lst/redline/ProtoObject;)V");
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitLineNumber(6, l1);
		mv.visitInsn(RETURN);
		mv.visitMaxs(3, 0);
		mv.visitEnd();
	}

	private void defineClassInitialization(ClassWriter classWriter, String qualifiedSubclass, String qualifiedSuperclass, String subclass) {
		MethodVisitor mv = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(14, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, qualifiedSuperclass, "<init>", "()V");
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitLineNumber(16, l1);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitTypeInsn(NEW, "st/redline/Metaclass");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "st/redline/Metaclass", "<init>", "()V");
		mv.visitFieldInsn(PUTFIELD, classQualifiedSubclass, "_class_", "Lst/redline/Metaclass;");
		Label l2 = new Label();
		mv.visitLabel(l2);
		mv.visitLineNumber(17, l2);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitLdcInsn(Type.getType("L"+subclass+";"));
		mv.visitMethodInsn(INVOKESTATIC, "st/redline/ProtoObject", "methodsFrom", "(Ljava/lang/Class;)Ljava/util/Map;");
		mv.visitFieldInsn(PUTFIELD, classQualifiedSubclass, "_methods_", "Ljava/util/Map;");
		Label l3 = new Label();
		mv.visitLabel(l3);
		mv.visitLineNumber(14, l3);
		mv.visitInsn(RETURN);
		Label l4 = new Label();
		mv.visitLabel(l4);
		mv.visitLocalVariable("this", "L"+classQualifiedSubclass+";", null, l0, l4, 0);
		mv.visitMaxs(3, 1);
		mv.visitEnd();
	}

	private void defineInstanceInitialization(ClassWriter classWriter, String qualifiedSubclass, String qualifiedSuperclass) {
		MethodVisitor mv = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(6, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, qualifiedSuperclass, "<init>", "()V");
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitLineNumber(8, l1);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitLdcInsn(subclass);
		mv.visitMethodInsn(INVOKESTATIC, "st/redline/Smalltalk", "classNamed", "(Ljava/lang/String;)Lst/redline/ProtoObject;");
		mv.visitTypeInsn(CHECKCAST, classQualifiedSubclass);
		mv.visitFieldInsn(PUTFIELD, qualifiedSubclass, "_class_", "Lst/redline/Object$mClass;");
		Label l2 = new Label();
		mv.visitLabel(l2);
		mv.visitLineNumber(6, l2);
		mv.visitInsn(RETURN);
		Label l3 = new Label();
		mv.visitLabel(l3);
		mv.visitLocalVariable("this", "L"+classQualifiedSubclass+";", null, l0, l3, 0);
		mv.visitMaxs(2, 1);
		mv.visitEnd();
	}

	private void defineClassFields(ClassWriter classWriter, String qualifiedSubclass, String qualifiedSuperclass) {
		FieldVisitor fv = classWriter.visitField(ACC_PUBLIC, "_class_", "Lst/redline/Metaclass;", null, null);
		fv.visitEnd();
	}

	private void defineInstanceFields(ClassWriter classWriter, String qualifiedSubclass, String qualifiedSuperclass) {
		FieldVisitor fv = classWriter.visitField(ACC_PUBLIC, "_class_", "L"+classQualifiedSubclass+";", null, null);
		fv.visitEnd();
	}

	private void defineClass(String sourcePath, ClassWriter classWriter, String subclass, String superclass) {
		classWriter.visit(V1_5, ACC_PUBLIC + ACC_SUPER, subclass, null, superclass, null);
		classWriter.visitSource(sourcePath, null);
		classWriter.visitInnerClass(qualifiedSubclass + "$mClass", qualifiedSubclass, "mClass", ACC_PUBLIC + ACC_STATIC);
	}

	public byte[][] definedClassBytes() {
		classWriter.visitEnd();
		classClassWriter.visitEnd();
		return new byte[][] { classWriter.toByteArray(), classClassWriter.toByteArray() };
	}

	public Class definedClass() {
		byte[][] code = definedClassBytes();
		defineClass(DEFAULT_JAVA_PACKAGE + subclass + "$mClass", code[1], 0, code[1].length);
		return defineClass(DEFAULT_JAVA_PACKAGE + subclass, code[0], 0, code[0].length);
	}

//	private void defineClassConstructor(ClassDefinition classDefinition) {
//		MethodVisitor mv = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
//		mv.visitCode();
////		defineClassInitialization(mv, classDefinition);
////		emitUnarySend(mv, classDefinition.unarySend());
////		emitKeywordSend(mv, classDefinition.keywordSends());
////		mv.visitInsn(POP);
//		mv.visitInsn(RETURN);
//		mv.visitMaxs(1, 1);
//		mv.visitEnd();
//	}

	private void emitMessage(MethodVisitor mv, String message) {
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(1, l0);
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn(message);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
	}

	private void emitKeywordSend(MethodVisitor mv, List<KeywordSend> keywordSends) {
		emitKeywordSendSignature(mv, keywordSends);
		emitKeywordSendArguments(mv, keywordSends);
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(keywordSends.get(0).keyword().beginLine, l0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "st/redline/ProtoObject", "prim$end", SEND_SIGNATURE[keywordSends.size()]);
	}

	private void emitKeywordSendArguments(MethodVisitor mv, List<KeywordSend> keywordSends) {
		for (KeywordSend keywordSend : keywordSends)
			for (MessageSend messageSend : keywordSend.arguments())
				emitMessageSend(mv, messageSend);
	}

	private void emitKeywordSendSignature(MethodVisitor mv, List<KeywordSend> keywordSends) {
		StringBuffer keywords = new StringBuffer(128);
		for (KeywordSend keywordSend : keywordSends)
			keywords.append(keywordSend.keyword().toString());
		mv.visitLdcInsn(keywords.toString());
	}

	private void emitMessageSend(MethodVisitor mv, MessageSend messageSend) {
		if (messageSend instanceof UnarySend) {
			emitUnarySend(mv, (UnarySend) messageSend);
		} else {
			throw new RuntimeException("Need to handle MessageSend of type: " + messageSend.getClass());
		}
	}

	private void emitUnarySend(MethodVisitor mv, UnarySend unarySend) {
		emitPrimary(mv, unarySend.primary());
		for (Token selector : unarySend.selectors())
			emitUnaryCall(mv, selector);
	}

	private void emitUnaryCall(MethodVisitor mv, Token selector) {
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(selector.beginLine, l0);
		mv.visitLdcInsn(selector.toString());
		mv.visitMethodInsn(INVOKEVIRTUAL, "st/redline/ProtoObject", "prim$end", SEND_SIGNATURE[0]);
	}

	private void emitPrimary(MethodVisitor mv, Primary primary) {
		if (primary instanceof PrimaryVariable) {
			emitPrimary(mv, (PrimaryVariable) primary);
		} else if (primary instanceof PrimaryLiteral) {
			emitPrimary(mv, (PrimaryLiteral) primary);
		} else {
			throw new RuntimeException("Need to handle PRIMARY of type: " + primary.getClass());
		}
	}

	private void emitPrimary(MethodVisitor mv, PrimaryVariable primaryVariable) {
		Variable variable = primaryVariable.variable();
		if (variable.isClass()) {
			emitClassLoopkup(mv, variable);
		} else {
			throw new RuntimeException("Need to handle other types of VARIABLE: " + variable.getClass() + " " + variable.toString());
		}
	}

	private void emitPrimary(MethodVisitor mv, PrimaryLiteral primaryLiteral) {
		Literal literal = primaryLiteral.literal();
		if (literal instanceof SymbolLiteral) {
			emitLiteral(mv, (SymbolLiteral) literal);
		} else if (literal instanceof StringLiteral) {
			emitLiteral(mv, (StringLiteral) literal);
		} else {
			throw new RuntimeException("Need to handle other types of LITERAL: " + literal.getClass());
		}
	}

	private void emitLiteral(MethodVisitor mv, SymbolLiteral symbolLiteral) {
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(symbolLiteral.lineNumber(), l0);
		mv.visitLdcInsn(symbolLiteral.toString());
		mv.visitInsn(ICONST_1);
		mv.visitMethodInsn(INVOKESTATIC, "st/redline/ProtoObject", "objectFromPrimitive", "(Ljava/lang/String;Z)Lst/redline/ProtoObject;");
	}

	private void emitLiteral(MethodVisitor mv, StringLiteral stringLiteral) {
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(stringLiteral.lineNumber(), l0);
		mv.visitLdcInsn(stringLiteral.toString());
		mv.visitInsn(ICONST_0);
		mv.visitMethodInsn(INVOKESTATIC, "st/redline/ProtoObject", "objectFromPrimitive", "(Ljava/lang/String;Z)Lst/redline/ProtoObject;");
	}

	private void emitClassLoopkup(MethodVisitor mv, Variable variable) {
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(variable.lineNumber(), l0);
		mv.visitLdcInsn(variable.toString());
		mv.visitMethodInsn(INVOKESTATIC, "st/redline/Smalltalk", "classNamed", "(Ljava/lang/String;)Lst/redline/ProtoObject;");
	}

	public void defineMethods(String sourcePath, int classDefinitionLineNumber, List<Method> methods, boolean classMethods) {
		for (Method method : methods)
			defineMethod(sourcePath, classDefinitionLineNumber, method, classMethods);
	}

	private void defineMethod(String sourcePath, int classDefinitionLineNumber, Method method, boolean classMethods) {
		MessagePattern messagePattern = method.messagePattern();
		String methodName = messagePattern.pattern();
		MethodVisitor mv;
		if (classMethods)
			mv = classClassWriter.visitMethod(ACC_PUBLIC, methodName, METHOD_SIGNATURE[messagePattern.argumentCount()], null, null);
		else
			mv = classWriter.visitMethod(ACC_PUBLIC, methodName, METHOD_SIGNATURE[messagePattern.argumentCount()], null, null);
		mv.visitCode();
		if (method.pragmas()[0] != null)
			emitPragmas(mv, method.pragmas()[0]);
		emitTemporaries(mv, method.temporaries());
		if (method.pragmas()[1] != null)
			emitPragmas(mv, method.pragmas()[1]);
		emitStatements(mv, method.statements());
		// return this, although we should never reach these statements.
		mv.visitVarInsn(ALOAD, 0);
		mv.visitInsn(ARETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

	private void emitStatements(MethodVisitor mv, Statements statements) {
		for (Expression expression : statements.expressions())
			emitExpression(mv, expression);
	}

	private void emitExpression(MethodVisitor mv, Expression expression) {
		System.out.println("TODO - Expression");
	}

	private void emitTemporaries(MethodVisitor mv, List<Variable> variables) {
		System.out.println("TODO - Temporaries");
	}

	private void emitPragmas(MethodVisitor mv, Pragma pragma) {
		System.out.println("TODO - Pragmas");
	}

	private void emitNumber(MethodVisitor mv, int number) {
		switch (number) {
			case 0:
				mv.visitInsn(ICONST_0);
				break;
			case 1:
				mv.visitInsn(ICONST_1);
				break;
			case 2:
				mv.visitInsn(ICONST_2);
				break;
			case 3:
				mv.visitInsn(ICONST_3);
				break;
			case 4:
				mv.visitInsn(ICONST_4);
				break;
			case 5:
				mv.visitInsn(ICONST_5);
				break;
			default:
				if (number < 128)
					mv.visitIntInsn(BIPUSH, number);
				else
					mv.visitIntInsn(SIPUSH, number);
		}
	}
}
