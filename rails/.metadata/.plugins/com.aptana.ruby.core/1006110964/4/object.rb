class Object < BasicObject
  include Kernel

  ARGF = ARGF
  ARGV = []
  Addrinfo = Addrinfo
  ArgumentError = ArgumentError
  Array = Array
  BasicObject = BasicObject
  BasicSocket = BasicSocket
  Bignum = Bignum
  Binding = Binding
  CGI = CGI
  CROSS_COMPILING = nil
  Class = Class
  Comparable = Comparable
  Complex = Complex
  ConditionVariable = Thread::ConditionVariable
  Data = Data
  Date = Date
  DateTime = DateTime
  Delegator = Delegator
  Digest = Digest
  Dir = Dir
  ENV = {"PATH"=>"/Users/kkim/.rbenv/shims:/Users/kkim/workspace/golang/bin:/Users/kkim/local/bin:/usr/local/heroku/bin:/usr/local/sbin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/opt/X11/bin:/usr/local/go/bin:/Library/TeX/texbin", "HISTCONTROL"=>"ignorespace", "HISTSIZE"=>"10000", "JAVA_HOME"=>"/Library/Java/JavaVirtualMachines/jdk1.8.0_66.jdk/Contents/Home", "JAVA_STARTED_ON_FIRST_THREAD_32872"=>"1", "LANG"=>"en_US.UTF-8", "MAVEN_OPTS"=>"-Dmaven.artifact.threads=3", "HISTFILESIZE"=>"10000", "DISPLAY"=>"/private/tmp/com.apple.launchd.K7P2MZelkN/org.macosforge.xquartz:0", "PYTHONSTARTUP"=>"/Users/kkim/.pythonrc.py", "LOGNAME"=>"kkim", "PROMPT_COMMAND"=>"history -a; history -n; ", "XPC_SERVICE_NAME"=>"org.eclipse.platform.ide.223712", "PWD"=>"/Applications/Aptana.app/Contents/MacOS", "SHELL"=>"/bin/bash", "APTANA_VERSION"=>"3.6.0", "LSCOLORS"=>"Gxfxcxdxbxegedabagacad", "APP_ICON_32872"=>"../Resources/Eclipse.icns", "GOPATH"=>"/Users/kkim/workspace/golang", "USER"=>"kkim", "GOROOT"=>"/usr/local/Cellar/go/1.5.1/libexec", "TMPDIR"=>"/var/folders/z3/x_06q3jx0z16j3dbmtqmg1sr0000gp/T/", "SSH_AUTH_SOCK"=>"/private/tmp/com.apple.launchd.pp85KZ8xrg/Listeners", "XPC_FLAGS"=>"0x0", "LC_ALL"=>"en_US.UTF-8", "HH_CONFIG"=>"hicolor", "__CF_USER_TEXT_ENCODING"=>"0x1F6:0x0:0x0", "Apple_PubSub_Socket_Render"=>"/private/tmp/com.apple.launchd.a7ns5FjZmg/Render", "HOME"=>"/Users/kkim", "SHLVL"=>"1"}
  EOFError = EOFError
  Encoding = Encoding
  EncodingError = EncodingError
  Enumerable = Enumerable
  Enumerator = Enumerator
  Errno = Errno
  Etc = Etc
  Exception = Exception
  FALSE = false
  FalseClass = FalseClass
  Fcntl = Fcntl
  Fiber = Fiber
  FiberError = FiberError
  File = File
  FileTest = FileTest
  FileUtils = FileUtils
  Fixnum = Fixnum
  Float = Float
  FloatDomainError = FloatDomainError
  GC = GC
  Gem = Gem
  Hash = Hash
  IO = IO
  IOError = IOError
  IPSocket = IPSocket
  IndexError = IndexError
  Integer = Integer
  Interrupt = Interrupt
  Kernel = Kernel
  KeyError = KeyError
  LoadError = LoadError
  LocalJumpError = LocalJumpError
  Marshal = Marshal
  MatchData = MatchData
  Math = Math
  Method = Method
  Module = Module
  Monitor = Monitor
  MonitorMixin = MonitorMixin
  Mutex = Mutex
  NIL = nil
  NameError = NameError
  Net = Net
  NilClass = NilClass
  NoMemoryError = NoMemoryError
  NoMethodError = NoMethodError
  NotImplementedError = NotImplementedError
  Numeric = Numeric
  OUTPUT_PATH = "/Users/kkim/github/projects/rails/.metadata/.plugins/com.aptana.ruby.core/1006110964/4/"
  Object = Object
  ObjectSpace = ObjectSpace
  OpenSSL = OpenSSL
  OptParse = OptionParser
  OptionParser = OptionParser
  Proc = Proc
  Process = Process
  Queue = Thread::Queue
  RUBY_COPYRIGHT = "ruby - Copyright (C) 1993-2015 Yukihiro Matsumoto"
  RUBY_DESCRIPTION = "ruby 2.2.3p173 (2015-08-18 revision 51636) [x86_64-darwin14]"
  RUBY_ENGINE = "ruby"
  RUBY_PATCHLEVEL = 173
  RUBY_PLATFORM = "x86_64-darwin14"
  RUBY_RELEASE_DATE = "2015-08-18"
  RUBY_REVISION = 51636
  RUBY_VERSION = "2.2.3"
  Random = Random
  Range = Range
  RangeError = RangeError
  Rational = Rational
  RbConfig = RbConfig
  Regexp = Regexp
  RegexpError = RegexpError
  Resolv = Resolv
  RubyVM = RubyVM
  RuntimeError = RuntimeError
  STDERR = IO.new
  STDIN = IO.new
  STDOUT = IO.new
  ScanError = StringScanner::Error
  ScriptError = ScriptError
  SecureRandom = SecureRandom
  SecurityError = SecurityError
  Signal = Signal
  SignalException = SignalException
  SimpleDelegator = SimpleDelegator
  SizedQueue = Thread::SizedQueue
  Socket = Socket
  SocketError = SocketError
  StandardError = StandardError
  StopIteration = StopIteration
  String = String
  StringIO = StringIO
  StringScanner = StringScanner
  Struct = Struct
  Symbol = Symbol
  SyntaxError = SyntaxError
  SystemCallError = SystemCallError
  SystemExit = SystemExit
  SystemStackError = SystemStackError
  TCPServer = TCPServer
  TCPSocket = TCPSocket
  TOPLEVEL_BINDING = #<Binding:0x007f9e4b0bc368>
  TRUE = true
  TSort = TSort
  Tempfile = Tempfile
  Thread = Thread
  ThreadError = ThreadError
  ThreadGroup = ThreadGroup
  Time = Time
  Timeout = Timeout
  TimeoutError = Timeout::Error
  TracePoint = TracePoint
  TrueClass = TrueClass
  TypeError = TypeError
  UDPSocket = UDPSocket
  UNIXServer = UNIXServer
  UNIXSocket = UNIXSocket
  URI = URI
  UnboundMethod = UnboundMethod
  UncaughtThrowError = UncaughtThrowError
  ZeroDivisionError = ZeroDivisionError
  Zlib = Zlib
  fatal = nil



  protected


  private

  def DelegateClass(arg0)
  end

  def Digest(arg0)
  end

  def dir_names(arg0)
  end

  def file_name(arg0)
  end

  def get_classes
  end

  def grab_instance_method(arg0, arg1)
  end

  def print_args(arg0)
  end

  def print_instance_method(arg0, arg1)
  end

  def print_method(arg0, arg1, arg2, arg3, arg4, *rest)
  end

  def print_type(arg0)
  end

  def print_value(arg0)
  end

  def timeout(arg0, arg1, arg2, *rest)
  end

end
