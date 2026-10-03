# Standalone build of the C++ physics engine (JNI shared library) and its integration test.
# The server's Gradle build (`cd server && ./gradlew run`) compiles the engine on its own,
# so this Makefile is only needed for `make test` or for building the library by hand.
#
#   make          builds build/lib/<PhysicsEngine library> and build/lib/PhysicsEngine.jar
#   make test     builds, then runs test/TestPhysicsEngine.java against the library
#   make clean
#
# Requires JAVA_HOME (a JDK 17+) and a 64-bit g++/clang++. On Windows use MSYS2/MinGW-w64
# (`mingw32-make`), e.g. from Git Bash with C:\msys64\mingw64\bin on PATH.

ifeq ($(OS),Windows_NT)
    PLATFORM    = win32
    LIB_NAME    = PhysicsEngine.dll
    LDFLAGS     = -shared -static -static-libgcc -static-libstdc++
    CP_SEP      = ;
else
    UNAME_S := $(shell uname -s)
    ifeq ($(UNAME_S),Darwin)
        PLATFORM = darwin
        LIB_NAME = libPhysicsEngine.dylib
        JAVA_HOME ?= $(shell /usr/libexec/java_home)
    else
        PLATFORM = linux
        LIB_NAME = libPhysicsEngine.so
    endif
    LDFLAGS = -shared
    CP_SEP  = :
endif

CXX      ?= g++
CXXFLAGS  = -std=c++17 -O2 -fPIC -MMD -MP \
            -I"$(JAVA_HOME)/include" -I"$(JAVA_HOME)/include/$(PLATFORM)" \
            -I$(INCLUDE_DIR) -Isrc/main/native
JAVAC     = "$(JAVA_HOME)/bin/javac"
JAVA      = "$(JAVA_HOME)/bin/java"
JAR       = "$(JAVA_HOME)/bin/jar"

BUILD_DIR       = build
NATIVE_BUILD_DIR = $(BUILD_DIR)/native
CLASS_BUILD_DIR = $(BUILD_DIR)/classes
INCLUDE_DIR     = $(BUILD_DIR)/include
LIB_DIR         = $(BUILD_DIR)/lib
JNI_HEADER      = $(INCLUDE_DIR)/com_physics_Manager.h

NATIVE_SOURCES = $(wildcard src/main/native/*.cpp)
NATIVE_OBJECTS = $(patsubst src/main/native/%.cpp,$(NATIVE_BUILD_DIR)/%.o,$(NATIVE_SOURCES))

all: $(LIB_DIR)/$(LIB_NAME) $(LIB_DIR)/PhysicsEngine.jar

# javac -h generates the JNI header from the Java side of the bridge
$(JNI_HEADER): src/main/java/com/physics/Manager.java
	mkdir -p $(CLASS_BUILD_DIR) $(INCLUDE_DIR)
	$(JAVAC) --release 17 -h $(INCLUDE_DIR) -d $(CLASS_BUILD_DIR) $<

$(NATIVE_BUILD_DIR)/%.o: src/main/native/%.cpp $(JNI_HEADER)
	mkdir -p $(NATIVE_BUILD_DIR)
	$(CXX) $(CXXFLAGS) -c $< -o $@

$(LIB_DIR)/$(LIB_NAME): $(NATIVE_OBJECTS)
	mkdir -p $(LIB_DIR)
	$(CXX) -o $@ $^ $(LDFLAGS)

$(LIB_DIR)/PhysicsEngine.jar: $(JNI_HEADER)
	mkdir -p $(LIB_DIR)
	$(JAR) cf $@ -C $(CLASS_BUILD_DIR) .

test: all
	$(JAVAC) --release 17 -cp "$(CLASS_BUILD_DIR)" -d $(BUILD_DIR)/test test/TestPhysicsEngine.java
	$(JAVA) --enable-native-access=ALL-UNNAMED -Djava.library.path=$(LIB_DIR) \
		-cp "$(CLASS_BUILD_DIR)$(CP_SEP)$(BUILD_DIR)/test" TestPhysicsEngine

clean:
	rm -rf $(BUILD_DIR)

-include $(NATIVE_OBJECTS:.o=.d)

.PHONY: all test clean
