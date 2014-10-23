import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * The main window and driver code of the program. The application starts at main which 
 * creates the window and registers all the widgets.
 * addFile() adds a file to the list of files to run on.
 * removeFile() removes the selected files fromt he list of files.
 * run() runs the test n number of times and puts the results in results.
 * @author Garrett Smith, 3018390
 *
 */
public class Window extends ApplicationWindow {
  private TableViewer tableViewer;
  private ListViewer listViewer;
  
  private Spinner spinner;
  
  private List<File> files = new ArrayList<File>();
  
  private List<ResultSet> results = new ArrayList<ResultSet>();

  /**
   * Create the application window.
   */
  public Window() {
    super(null);
    setShellStyle(SWT.MAX);
    createActions();
    //addToolBar(SWT.FLAT | SWT.WRAP);
    //addMenuBar();
    addStatusLine();
  }

  /**
   * Create contents of the application window.
   * @param parent
   */
  @Override
  protected Control createContents(Composite parent) {
    Composite container = new Composite(parent, SWT.NONE);
    GridLayout gl_container = new GridLayout(5, false);
    gl_container.horizontalSpacing = 3;
    container.setLayout(gl_container);
    {
      listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
      listViewer.setContentProvider(new FileContentProvider());
      listViewer.setLabelProvider(new FileLabelProvider());
      listViewer.setInput(files);
      org.eclipse.swt.widgets.List list = listViewer.getList();
      list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 5, 1));
    }
    {
      Button btnAdd = new Button(container, SWT.NONE);
      GridData gd_btnAdd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
      gd_btnAdd.widthHint = 100;
      btnAdd.setLayoutData(gd_btnAdd);
      btnAdd.setText("Add");
      btnAdd.addMouseListener(new MouseListener() {

        @Override
        public void mouseDoubleClick(MouseEvent e) {
          // TODO Auto-generated method stub
          
        }

        @Override
        public void mouseDown(MouseEvent e) {
          // TODO Auto-generated method stub
          
        }

        @Override
        public void mouseUp(MouseEvent e) {
          addFile();
        }
        
      });
    }
    {
      Button btnRemove = new Button(container, SWT.NONE);
      GridData gd_btnRemove = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
      gd_btnRemove.widthHint = 100;
      btnRemove.setLayoutData(gd_btnRemove);
      btnRemove.setText("Remove");
      btnRemove.addMouseListener(new MouseListener() {

        @Override
        public void mouseDoubleClick(MouseEvent e) {
          // TODO Auto-generated method stub
          
        }

        @Override
        public void mouseDown(MouseEvent e) {
          // TODO Auto-generated method stub
          
        }

        @Override
        public void mouseUp(MouseEvent e) {
          removeFile();
        }
        
      });
    }
    {
      Label lblNewLabel = new Label(container, SWT.NONE);
      lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
      lblNewLabel.setText("Rounds");
    }
    {
      spinner = new Spinner(container, SWT.BORDER);
      spinner.setMinimum(1);
      spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    }
    {
      Button btnNewButton = new Button(container, SWT.NONE);
      GridData gd_btnNewButton = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
      gd_btnNewButton.widthHint = 100;
      btnNewButton.setLayoutData(gd_btnNewButton);
      btnNewButton.setText("Run");
      btnNewButton.addMouseListener(new MouseListener() {

        @Override
        public void mouseDoubleClick(MouseEvent e) {
          // TODO Auto-generated method stub
          
        }

        @Override
        public void mouseDown(MouseEvent e) {
          // TODO Auto-generated method stub
          
        }

        @Override
        public void mouseUp(MouseEvent e) {
          run(spinner.getSelection(), files);
        }
        
      });
    }
    {
      Composite composite = new Composite(container, SWT.NONE);
      composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
      TableColumnLayout tcl_composite = new TableColumnLayout();
      composite.setLayout(tcl_composite);
      {
        tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
        tableViewer.setLabelProvider(new ResultsLabelProvider());
        tableViewer.setContentProvider(new ResultsContentProvider());
        tableViewer.setInput(results);
        Table table = tableViewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        {
          TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
          tblclmnNewColumn.setResizable(false);
          tcl_composite.setColumnData(tblclmnNewColumn, new ColumnPixelData(180, true, true));
          tblclmnNewColumn.setText("File");
        }
        {
          TableColumn tblclmnSizepixels = new TableColumn(table, SWT.NONE);
          tblclmnSizepixels.setResizable(false);
          tcl_composite.setColumnData(tblclmnSizepixels, new ColumnPixelData(100, true, true));
          tblclmnSizepixels.setText("Size (pixels)");
        }
        {
          TableColumn tblclmnSizekb = new TableColumn(table, SWT.NONE);
          tblclmnSizekb.setResizable(false);
          tcl_composite.setColumnData(tblclmnSizekb, new ColumnPixelData(100, true, true));
          tblclmnSizekb.setText("Size (kb)");
        }
        {
          TableColumn tblclmnDesEncryption = new TableColumn(table, SWT.NONE);
          tblclmnDesEncryption.setResizable(false);
          tcl_composite.setColumnData(tblclmnDesEncryption, new ColumnPixelData(90, true, true));
          tblclmnDesEncryption.setText("Des Encryption");
        }
        {
          TableColumn tblclmnDesDecryption = new TableColumn(table, SWT.NONE);
          tblclmnDesDecryption.setResizable(false);
          tcl_composite.setColumnData(tblclmnDesDecryption, new ColumnPixelData(90, true, true));
          tblclmnDesDecryption.setText("DES Decryption");
        }
        {
          TableColumn tblclmnAesEncryption = new TableColumn(table, SWT.NONE);
          tblclmnAesEncryption.setResizable(false);
          tcl_composite.setColumnData(tblclmnAesEncryption, new ColumnPixelData(90, true, true));
          tblclmnAesEncryption.setText("AES Encryption");
        }
        {
          TableColumn tblclmnAesDecryption = new TableColumn(table, SWT.NONE);
          tblclmnAesDecryption.setResizable(false);
          tcl_composite.setColumnData(tblclmnAesDecryption, new ColumnPixelData(90, true, true));
          tblclmnAesDecryption.setText("AES Decryption");
        }
        {
          TableColumn tblclmnRsaEncrpytion = new TableColumn(table, SWT.NONE);
          tblclmnRsaEncrpytion.setResizable(false);
          tcl_composite.setColumnData(tblclmnRsaEncrpytion, new ColumnPixelData(90, true, true));
          tblclmnRsaEncrpytion.setText("RSA Encrpytion");
        }
        {
          TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
          tblclmnNewColumn_1.setResizable(false);
          tcl_composite.setColumnData(tblclmnNewColumn_1, new ColumnPixelData(90, true, true));
          tblclmnNewColumn_1.setText("RSA Decrpytion");
        }
      }
    }

    return container;
  }

  /**
   * Create the actions.
   */
  private void createActions() {
    // Create the actions
  }

  /**
   * Create the status line manager.
   * @return the status line manager
   */
  @Override
  protected StatusLineManager createStatusLineManager() {
    StatusLineManager statusLineManager = new StatusLineManager();
    return statusLineManager;
  }

  /**
   * Launch the application.
   * @param args
   */
  public static void main(String args[]) {
//    byte[] bytes = 
//      { -96,
//        120,
//        80,
//        96,
//        48,
//        40,
//        -24,
//        0,
//        40,
//        -56,
//        -64,
//        -56,
//        -8,
//        -56,
//        96,
//        -88,
//        0,
//        0,
//        80,
//        0,
//        80,
//        0,
//        0,
//        4 };
//    RSAKeyPair keys = RSA.generateKeyPair();
//    byte[] encrypted = RSA.encrypt(bytes, keys.getPrivateKey());
//    encrypted = RSA.decrypt(encrypted, keys.getPublicKey());
//    System.out.println(Arrays.equals(bytes, encrypted));
    try {
      Window window = new Window();
      window.setBlockOnOpen(true);
      window.open();
      Display.getCurrent().dispose();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Configure the shell.
   * @param newShell
   */
  @Override
  protected void configureShell(Shell newShell) {
   // newShell.setMinimumSize(new Point(1140, 400));
    super.configureShell(newShell);
    newShell.setText("Assignment 1, Garrett Smith 3018390");
    // hide the weird extra seperator
    getSeperator1().setVisible(false);
  }

  /**
   * Return the initial size of the window.
   */
  @Override
  protected Point getInitialSize() {
    return new Point(980, 400);
  }
  
  class FileLabelProvider extends LabelProvider {

    @Override
    public Image getImage(Object element) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public String getText(Object element) {
      return element.toString();
    }
    
  }
  
  class FileContentProvider implements IStructuredContentProvider {

    @Override
    public void dispose() {
      // TODO Auto-generated method stub
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      // TODO Auto-generated method stub
    }

    @Override
    public Object[] getElements(Object inputElement) {
      return ((List<File>)inputElement).toArray();
    }
    
  }
  
  class ResultSet {

    private File file;
    private double desEncrypt;
    private double desDecrypt; 
    private double aesEncrypt; 
    private double aesDecrypt; 
    private double rsaEncrypt; 
    private double rsaDecrypt;
    
    public ResultSet(
        File file, 
        double desEncrypt, 
        double desDecrypt, 
        double aesEncrypt, 
        double aesDecrypt, 
        double rsaEncrypt, 
        double rsaDecrypt) {
      this.file = file;
      this.desEncrypt = desEncrypt;
      this.desDecrypt = desDecrypt;
      this.aesEncrypt = aesEncrypt;
      this.aesDecrypt = aesDecrypt;
      this.rsaEncrypt = rsaEncrypt;
      this.rsaDecrypt = rsaDecrypt;
    }
    
    public String getFileName() {
      return file.getName();
    }
    
    public long getFileSize() {
      return file.length();
    }
    
    public int getPixels() {
      try {
      ImageData data = new ImageData(file.getPath());
      return data.width * data.height;
      }
      catch(SWTException e) {
        return -1;
      }
    }

    public double getDesEncrypt() {
      return desEncrypt;
    }

    public double getDesDecrypt() {
      return desDecrypt;
    }

    public double getAesEncrypt() {
      return aesEncrypt;
    }

    public double getAesDecrypt() {
      return aesDecrypt;
    }

    public double getRsaEncrypt() {
      return rsaEncrypt;
    }

    public double getRsaDecrypt() {
      return rsaDecrypt;
    }
    
  }
  
  class ResultsLabelProvider extends LabelProvider implements ITableLabelProvider {

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {
      ResultSet res = (ResultSet) element;
      String rtn;
      switch (columnIndex) {
        case 0:
          rtn = res.getFileName();
          break;
        case 1: 
          int pixels = res.getPixels();
          rtn = (pixels == -1 ? "-" : Integer.toString(pixels) + " pixels");
          break;
        case 2:
          rtn = Long.toString(res.getFileSize()) + " kb";
          break;
        case 3:
          rtn = Double.toString(res.getDesEncrypt()) + " ms";
          break;
        case 4:
          rtn = Double.toString(res.getDesDecrypt()) + " ms";
          break;
        case 5:
          rtn = Double.toString(res.getAesEncrypt()) + " ms";
          break;
        case 6:
          rtn = Double.toString(res.getAesDecrypt()) + " ms";
          break;
        case 7:
          rtn = Double.toString(res.getRsaEncrypt()) + " ms";
          break;
        case 8:
          rtn = Double.toString(res.getRsaDecrypt()) + " ms";
          break;
        default:
          // should not get here
          rtn = "";
      }
      return rtn;
    }
    
  }
  
  class ResultsContentProvider implements IStructuredContentProvider {

    @Override
    public void dispose() {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public Object[] getElements(Object inputElement) {
      return ((List<ResultSet>) inputElement).toArray();
    }
  }
  
  /**
   * Add a file to the list of files to run on.
   */
  public void addFile() {
    // create the dialog to select an image file
    FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
    dialog.setText("Select a file");
    
    // get a file path
    String path = dialog.open();

    // open the file with the application
    if (path != null) {
      // prevent duplicates
      for (File file : files) {
        if (file.getPath().equals(path)) {
          return;
        }
      }
      // add the file and refresh the list
      files.add(new File(path));
      listViewer.refresh();
    }
  }
  
  /**
   * Remove a file from the list of files to run on.
   */
  public void removeFile() {
    // remove all selected files
    StructuredSelection selection = (StructuredSelection) listViewer.getSelection();
    for (Object obj : selection.toList()) {
      File file = (File) obj;
      files.remove(file);
    }
    listViewer.refresh();
  }
  
  /**
   * Run the encryption and decryption tests n times on the given files.
   * @param n
   * @param files
   */
  public void run(int n, List<File> files) {
    //long startTime = System.currentTimeMillis();
    
    Random rand = new Random();
    
    // result times
    Map<File, Double> desEncryptTimes = new HashMap<File, Double>();
    Map<File, Double> aesEncryptTimes = new HashMap<File, Double>();
    Map<File, Double> rsaEncryptTimes = new HashMap<File, Double>();    

    Map<File, Double> desDecryptTimes = new HashMap<File, Double>();
    Map<File, Double> aesDecryptTimes = new HashMap<File, Double>();
    Map<File, Double> rsaDecryptTimes = new HashMap<File, Double>();
    
    // keys
    List<byte[]> desKeys = new ArrayList<byte[]>(n);
    List<byte[]> aesKeys = new ArrayList<byte[]>(n);
    List<RSAKeyPair> rsaKeys = new ArrayList<RSAKeyPair>(n);
    
    // generate keys
    for (int i = 0; i < n; i++) {
      desKeys.add(i, makeDESKey(rand));
      aesKeys.add(i, makeAESKey(rand));
      rsaKeys.add(i, RSA.generateKeyPair());
    }
    
    // run for each file
    for (File file : files) {
      try {
        // read bytes from file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        // create a byte array of the size of the file round to the nearest 
        // multiple of eight
        byte[] bytes = new byte[(int) (f.length() + 7) / 8 * 8];
        f.read(bytes);
        f.close();

        // des
        long desEncryptTotal = 0;
        long desDecryptTotal = 0;
        
        for (byte[] key : desKeys) {
          DES des = new DES(key);
          
          long encryptStart = System.currentTimeMillis();
          byte[] encrypted = des.encrypt(bytes);
          desEncryptTotal += System.currentTimeMillis() - encryptStart;
          
          long decryptStart = System.currentTimeMillis();
          des.decrypt(encrypted);
          desDecryptTotal += System.currentTimeMillis() - decryptStart;
        }
        
        // save average time
        desEncryptTimes.put(file, new Double((double)desEncryptTotal / n));
        desDecryptTimes.put(file, new Double((double)desDecryptTotal / n));
        
        // aes
        long aesEncryptTotal = 0;
        long aesDecryptTotal = 0;
        
        for (byte[] key : aesKeys) {          
          long encryptStart = System.currentTimeMillis();
          byte[] encrypted = AES.encrypt(bytes, key);
          aesEncryptTotal += System.currentTimeMillis() - encryptStart;
          
          long decryptStart = System.currentTimeMillis();
          AES.decrypt(encrypted, key);
          aesDecryptTotal += System.currentTimeMillis() - decryptStart;
        }
        
        // save average time
        aesEncryptTimes.put(file, new Double((double)aesEncryptTotal / n));
        aesDecryptTimes.put(file, new Double((double)aesDecryptTotal / n));
        
        // rsa
        long rsaEncryptTotal = 0;
        long rsaDecryptTotal = 0;
        
        for (RSAKeyPair keys : rsaKeys) {          
          long encryptStart = System.currentTimeMillis();
          byte[] encrypted = RSA.encrypt(bytes, keys.getPrivateKey());
          rsaEncryptTotal += System.currentTimeMillis() - encryptStart;
          
          long decryptStart = System.currentTimeMillis();
          encrypted = RSA.decrypt(encrypted, keys.getPublicKey());
          rsaDecryptTotal += System.currentTimeMillis() - decryptStart;
        }
        
        // save average time
        rsaEncryptTimes.put(file, new Double((double)rsaEncryptTotal / n));
        rsaDecryptTimes.put(file, new Double((double)rsaDecryptTotal / n));

      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
    // save total times
    //long endTime = System.currentTimeMillis();
    //long runTime = endTime - startTime;
    
    // save results
    results.clear();
    for (File file : files) {
      ResultSet res = new ResultSet(
              file, 
              desEncryptTimes.get(file),
              desDecryptTimes.get(file),
              aesEncryptTimes.get(file),
              aesDecryptTimes.get(file),
              rsaEncryptTimes.get(file),
              rsaDecryptTimes.get(file));
      results.add(res);      
    }
    tableViewer.refresh();
  }
  
  /**
   * Creates a random DES key. 
   * Parity bits are not set properly but this doesn't matter.
   * @param rand
   * @return
   */
  private static byte[] makeDESKey(Random rand) {
    return randomBytes(8, rand);
  }
  
  /**
   * Creates a random AES key.
   * @param rand
   * @return
   */
  private static byte[] makeAESKey(Random rand) {
    return randomBytes(16, rand);
  }
  
  /**
   * Generates a new byte array of the given length filled with random data.
   * @param length
   * @param rand
   * @return
   */
  private static byte[] randomBytes(int length, Random rand) {
    byte[] bytes = new byte[length];
    // fill with random bytes
    rand.nextBytes(bytes);
    return bytes;
  }

}
