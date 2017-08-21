## AWS Lessons Learned
A few lessons I learned while playing around with the AWS free tier services.

### Amazon RDS instance billing

**Issue**: RDS instance usage seemed to increase too fast, even if the instances were not actually *used*.  
**Reason**: The RDS instances are actually billed per hour of *being started*, not the *being used* I was expecting.  
**Solution**: Created an Amazon Lambda function to shut down the RDS instances every evening.
**References**:
* [Start/Stop RDS instances](https://www.codeproject.com/Articles/1190194/Start-Stop-RDS-instances-on-schedule)
* [Cutting RDS instance costs in half](https://medium.com/cognitoiq/low-hanging-fruit-cutting-your-rds-instance-costs-in-half-736b8b490a24)

### Amazon QuickSight billing

**Issue**: After playing around with QuickSight and an RDS dataset, when I came back from the weekend I owed AWS 2$ for surpassing the free tier resources.  
**Reason**: Mostly unknown to this date. According to the Amazon Billing Service, a DMS instance was started during the weekend and keeping it up was not included in the free tier offer. I assumed the QuickSight needed the DMS instance to migrate stuff from the RDS instance. However, for it to start it by it's own... that was not nice at all.  
**Solution**: More of a workaround: Deleted the QuickSight account and everything in DMS: endpoints, tasks, certificates, everything. The costs continued to raise for half a day, then they stopped. </3 AWS.  

### Amazon RDS storage - 20GB for a MB database.

**Issue**: The RDS storage claims to be approaching the 20GB limit. My database has one table with 4 columns and 5 rows :|.  
**Reason**:

