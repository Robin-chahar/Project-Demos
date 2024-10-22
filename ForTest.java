import java.util.Scanner;
class InsufficientFundsException extends Exception{
    public InsufficientFundsException(String message) {
        super(message);
    }
}
 class Account {
   private String accNo;
   private String accHolderName;
   private String accType;
   private int accBalance;

   public String getAccNo() {
       return accNo;
   }
   public void setAccNo(String accNo) {
       this.accNo = accNo;
   }

   public String getAccHolderName() {
       return accHolderName;
   }

   public void setAccHolderName(String accHolderName) {
       this.accHolderName = accHolderName;
   }

   public String getAccType() {
       return accType;
   }

   public void setAccType(String accType) {
       this.accType = accType;
   }

   public int getAccBalance() {
       return accBalance;
   }
   public void setAccBalance(int accBalance) {
       this.accBalance = accBalance;
   }
}

 class Transaction {

   //instance variable/property
   private String transactionId;
    
   //parameterized constructor
   public Transaction(String transactionId) {
       this.transactionId = transactionId;
   }

   public void withdraw(Account account, int withdrawAmount){
       try {
           System.out.println("Transaction Details");
           System.out.println("--------------------------");
           System.out.println("Transaction Id       : "+transactionId);
           System.out.println("Account Number       : "+account.getAccNo());
           System.out.println("Account Holder Name  : "+account.getAccHolderName());
           System.out.println("Account Type         : "+account.getAccType());
           System.out.println("Transaction Type     : WITHDRAW");
           int accBalance = account.getAccBalance();

           if(accBalance < withdrawAmount){
               System.out.println("Total Balance       : "+account.getAccBalance());
               System.out.println("Transaction Status  : FAILURE");
   throw new InsufficientFundsException("Funds Are not sufficient in your Account");
           }else{
               int newBalance = accBalance - withdrawAmount;
               account.setAccBalance(newBalance);
               System.out.println("Total Balance       : "+account.getAccBalance());
               System.out.println("Transaction Status  : SUCCESS");
           }

       }catch (InsufficientFundsException e){
           System.out.println("Reason   : "+e.getMessage());
       }finally{
           System.out.println("***********ThankQ, Visit Again*************");
       }
   }
}

public class ForTest {
   public static void main(String[] args) {
       
       Account account1 = new Account();
       account1.setAccNo("a111");
       account1.setAccHolderName("DD");
       account1.setAccType("Savings");
       account1.setAccBalance(20000);

       Transaction transaction1 = new Transaction("t111");
       transaction1.withdraw(account1, 5000);

       Account account2 = new Account();
       account2.setAccNo("a222");
       account2.setAccHolderName("Anil");
       account2.setAccType("Savings");
       account2.setAccBalance(10000);

       Transaction transaction2 = new Transaction("t222");
       transaction2.withdraw(account2, 15000);

   }
}
