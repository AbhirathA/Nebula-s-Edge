CXX = g++
CXXFLAGS = -std=c++17 -fPIC \
           -I/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/include \
           -I/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home/include/darwin \
           -Isrc/main/include \
           -Isrc/main/native
LDFLAGS = -shared
SRC_DIR = src/main/native
BUILD_DIR = build
LIB_DIR = lib
LIB_NAME = libPhysicsEngine.dylib  # libPhysicsEngine.so for Linux, libPhysicsEngine.dll for Windows

SOURCES = $(wildcard $(SRC_DIR)/*.cpp)
OBJECTS = $(patsubst $(SRC_DIR)/%.cpp,$(BUILD_DIR)/%.o,$(SOURCES))

all: $(LIB_DIR)/$(LIB_NAME)

$(BUILD_DIR)/%.o: $(SRC_DIR)/%.cpp
	mkdir -p $(BUILD_DIR)
	$(CXX) $(CXXFLAGS) -c $< -o $@

$(LIB_DIR)/$(LIB_NAME): $(OBJECTS)
	mkdir -p $(LIB_DIR)
	$(CXX) $(LDFLAGS) -o $@ $^

clean:
	rm -rf $(BUILD_DIR)/*.o $(LIB_DIR)/$(LIB_NAME)
