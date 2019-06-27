/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.devctrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.devctrl.pojo.InitialDeviceConfig;
import msf.ecmm.ope.receiver.pojo.parts.Ztp;

/**
 * Device initial config file creation.
 */
public class CreateInitialDeviceConfig {
  /**
   * Logger.
   */
  private final MsfLogger logger = new MsfLogger();

  /** Self(singleton) */
  private static CreateInitialDeviceConfig me = new CreateInitialDeviceConfig();

  /** Template Replacement Keyword  Device management IF address. */
  private static final String TEMPLATE_KEYWORD_DEVICEMANAGEMENTADDRESS = "$$DEVICEMANAGEMENTADDRESS$$";

  /** Template Replacement Keyword  NTP server address. */
  private static final String TEMPLATE_KEYWORD_NTPSERVER = "$$NTPSERVER$$";

  /** Template Replacement Keyword  Device subnet mask. */
  private static final String TEMPLATE_KEYWORD_SUBNETMASK = "$$SUBNETMASK$$";

  /** Template Replacement Keyword  device max prefix number. */
  private static final String DEVICEMANAGEMENT_CIDRADDRESS = "$$DEVICEMANAGEMENT_CIDRADDRESS$$";

  /** Template Replacement Keyword  EC management IF. */
  private static final String TEMPLATE_KEYWORD_ECMANAGEMENTADDRESS = "$$ECMANAGEMENTADDRESS$$";

  /** Template Replacement Keyword  BGP community value. */
  private static final String TEMPLATE_KEYWORD_COMMUNITYMEMBERS = "$$COMMUNITYMEMBERS$$";

  /** Template Replacement Keyword  BGP Master / Slave community value. */
  private static final String TEMPLATE_KEYWORD_BELONGINGSIDEMEMBERS = "$$BELONGINGSIDEMEMBERS$$";

  /**
   * Constructor.
   */
  private CreateInitialDeviceConfig() {

  }

  /**
   * Getting instance. 
   *
   * @return self 
   */
  public static CreateInitialDeviceConfig getInstance() {
    return me;
  }

  /**
   * Device initial config file creation.
   *
   * @param info
   *          Device initial config file information
   * @throws DevctrlException
   *           error has occurred in Device initial config file creation
   */
  public void create(InitialDeviceConfig info) throws DevctrlException {
    logger.debug("initialConfig create info =" + info);
    try {
      String ecMngAddr = EcConfiguration.getInstance().get(String.class, EcConfiguration.DEVICE_EC_MANAGEMENT_IF);
      String mngIfAdd = info.getDeviceManagementAddress();
      int mngPrefix = info.getDevicemanagementCidraddress();
      String devicemanagementCidraddress = ipCalculation(mngIfAdd, mngPrefix);
      String subnetmask = subnetmaskCalculation(mngPrefix);
      HashMap<String, String> replaceKeys = new HashMap<>();
      replaceKeys.put(TEMPLATE_KEYWORD_DEVICEMANAGEMENTADDRESS, info.getDeviceManagementAddress());
      replaceKeys.put(DEVICEMANAGEMENT_CIDRADDRESS, devicemanagementCidraddress);
      replaceKeys.put(TEMPLATE_KEYWORD_SUBNETMASK, subnetmask);
      replaceKeys.put(TEMPLATE_KEYWORD_ECMANAGEMENTADDRESS, ecMngAddr);
      replaceKeys.put(TEMPLATE_KEYWORD_NTPSERVER, info.getNtpServerAddress());
      if (!(info.getCommmunityMembers() == null) && !(info.getBelongingSideMembers() == null)) {
        replaceKeys.put(TEMPLATE_KEYWORD_COMMUNITYMEMBERS, info.getCommmunityMembers());
        replaceKeys.put(TEMPLATE_KEYWORD_BELONGINGSIDEMEMBERS, info.getBelongingSideMembers());
      }

      File template = new File(info.getConfigTemplate());
      if (template.exists() && template.isFile()) {
        logger.debug("template is file.");
        exportFile(new File(info.getConfigTemplate()), replaceKeys, new File(info.getInitialConfig()));
      } else {
        logger.debug("template is not file.");
        String[] templateDirs = info.getConfigTemplate().split(Ztp.INITIAL_DIR_DELIMITER);
        String[] initialDirs = info.getInitialConfig().split(Ztp.INITIAL_DIR_DELIMITER);
        if (templateDirs.length != initialDirs.length) {
          logger.debug("Unmatch dirctory number.");
          throw new IOException();
        }
        boolean isFile = false;
        for (int i = 0; i < templateDirs.length; i++) {
          List<File> templateFiles = getFileList(templateDirs[i]);
          for (File templateFile : templateFiles) {
            File outfile = new File(initialDirs[i] + "/" + templateFile.getName());
            exportFile(templateFile, replaceKeys, outfile);
            isFile = true;
          }
        }
        if (!isFile) {
          logger.debug("File not found.");
          throw new FileNotFoundException();
        }
      }
    } catch (Exception e1) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_403041, e1));
      throw new DevctrlException("Could not create initial config ");
    }
  }

  /**
   * File export.
   * @param template input template file
   * @param replaceKeys  replacement keyword
   * @param outfile  Output file
   * @throws Exception  At the time of exception occurrence
   */
  private void exportFile(File template, HashMap<String, String> replaceKeys, File outfile) throws Exception {
    List<String> templateLines = Files.readAllLines(template.toPath());
    ArrayList<String> rep = replace(replaceKeys, templateLines);
    Files.write(outfile.toPath(), rep);
    Set<PosixFilePermission> filePermission = new HashSet<PosixFilePermission>();
    filePermission.add(PosixFilePermission.OWNER_READ);
    filePermission.add(PosixFilePermission.OWNER_WRITE);
    filePermission.add(PosixFilePermission.GROUP_READ);
    filePermission.add(PosixFilePermission.OTHERS_READ);
    filePermission.add(PosixFilePermission.OWNER_EXECUTE);
    filePermission.add(PosixFilePermission.GROUP_EXECUTE);
    filePermission.add(PosixFilePermission.OTHERS_EXECUTE);
    Files.setPosixFilePermissions(outfile.toPath(), filePermission);
    logger.debug("initial config file create. " + template + " -> " + outfile);
  }

  /**
   * File list creation.
   * @param dirName  Directory name
   * @return  File list
   */
  private List<File> getFileList(String dirName) {
    List<File> ret = new ArrayList<>();
    File dir = new File(dirName);
    for (File file : dir.listFiles()) {
      if (file.isFile()) {
        ret.add(file);
      }
    }
    return ret;
  }

  /**
   * Templace replacement.
   *
   * @param keys
   *          replacement character string
   * @param lines
   *          template
   * @return character string after the replacement
   */
  private ArrayList<String> replace(HashMap<String, String> keys, List<String> lines) {
    ArrayList<String> ret = new ArrayList<>();
    for (String line : lines) {
      for (Entry<String, String> entry : keys.entrySet()) {
        line = line.replace(entry.getKey(), entry.getValue());
      }
      ret.add(line);
    }
    return ret;
  }

  /**
   * IP address calclation.
   *
   * @param mngIfAdd
   *          management IP address
   * @param mngPrefix
   *          management IP prefix
   * @return IpPrefix
   */
  public String ipCalculation(String mngIfAdd, int mngPrefix) {
    String slash = "/";
    String ipPrefix = mngIfAdd.concat(slash).concat(String.valueOf(mngPrefix));
    return ipPrefix;
  }

  /**
   * Subnet mask calclation.
   *
   * @param mngPrefix
   *          management prefix
   * @return subnet mask
   */
  public static String subnetmaskCalculation(int mngPrefix) {

    if (mngPrefix < 0 || mngPrefix > 32) {
      throw new IllegalArgumentException((" + mngPrefix + "));
    }

    final StringBuilder subnetBinary = new StringBuilder(Long.toBinaryString((long) (Math.pow(2, mngPrefix) - 1)));
    for (int i = subnetBinary.length(); i <= 32; i++) {
      subnetBinary.append("0");
    }

    final StringBuilder result = new StringBuilder();
    for (int i = 8, length = subnetBinary.length(); i <= length; i += 8) {
      result.append(Integer.parseInt(subnetBinary.substring(i - 8, i), 2)).append(".");
    }
    result.setLength(result.length() - 1);
    return result.toString();
  }
}
