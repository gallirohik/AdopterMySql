package com.mysql.adaptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;

import com.mysql.adaptor.annotation.Metric;

public class MysqlMetrics<fullscan_utilization> {
	public Connection con = null;
	public Instant start = Instant.now();
	public float lastScan = fullscan_utilization();

	@Metric(name = "global")
	public void global() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?useSSL=false", "root", "root");
			Statement st = con.createStatement(); 
			String sql = "SHOW GLOBAL STATUS";
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				while (rs.next()) {
					System.out.println(rs.getString(1) + "-----" + rs.getString(2));
				}
			} else {
				System.out.println("Something went Wrong..!");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	@Metric(name = "slave_status")
	public void slave_status() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/performance_schema?useSSL=false", "root",
					"1234");
			Statement st = con.createStatement();
			String sql = "SELECT * FROM replication_connection_status;";
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				while (rs.next()) {
					System.out.println(rs.getString(1) + "----- " + rs.getString(2));
				}
			} else {
				System.out.println("Something went Wrong..!");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	@Metric(name = "openfile_utilization")
	public void openFile_utilization() {
		String str = "Opened_files";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/performance_schema?useSSL=false", "root",
					"1234");
			Statement st = con.createStatement();
			String sql = "show  global status  like 'open_%'";
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				while (rs.next()) {
					if (rs.getString(1).equals(str)) {
						System.out.println(rs.getString(2));
					}
				}
			} else {
				System.out.println("Something went Wrong..!");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	@Metric(name = "fullscan_utilization")
	public float fullscan_utilization() {
		String str1 = "Handler_read_rnd", str2 = "Handler_read_rnd_next";
		float sum1 = 0, sum2 = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/performance_schema?useSSL=false", "root",
					"1234");
			Statement st = con.createStatement();
			String sql = "show  global status  like 'handler_read_%'";
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				while (rs.next()) {
					if (rs.getString(1).equals(str1) || rs.getString(1).equals(str2)) {
						sum2 = sum2 + rs.getFloat(2);
					}
					sum1 = sum1 + rs.getFloat(2);
				}
				// System.out.println(sum1+" "+sum2+" "+(sum2/sum1)*100);
			} else {
				System.out.println("Something went Wrong..!");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
		return (sum2 / sum1) * 100;
	}

	@Metric(name = "temp_table_utilization")
	public void temp_tables_utilization() {
		String str1 = "Created_tmp_disk_tables";
		String str2 = "Created_tmp_tables";
		float sum1 = 0, sum2 = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://192.168.42.168:3306/performance_schema?useSSL=false", "root",
					"1234");
			Statement st = con.createStatement();
			String sql = "show global status like 'Created_%'";
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				while (rs.next()) {
					// if(rs.getString(1).equals(str1)){System.out.println(rs.getString(1)+"
					// "+rs.getFloat(2));}
					System.out.println(rs.getString(1) + " " + rs.getString(2));
					// if(rs.getString(1).equals(str2)){System.out.println(rs.getString(1)+"
					// "+rs.getFloat(2));}
				}

			} else {
				System.out.println("Something went Wrong..!");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	@Metric(name = "delta_utilization")
	public void delta() {
		float presentScan = fullscan_utilization();
		Instant end = Instant.now();
		Myfile file = new Myfile();
		String str = file.read();
		String result[] = str.split(" ");
		String str2 = result[1];
		Float f = new Float(str2);
		lastScan = f.parseFloat(str2);
		String start1 = result[0];
		start = Instant.parse(start1);
		Duration timeElapsed = Duration.between(start, end);
		System.out.println(((presentScan - lastScan) / timeElapsed.toMillis()) * 1000);
		System.out.println("Time taken: " + timeElapsed.toMillis() + " milliseconds");
		start = end;
		lastScan = presentScan;
		String str1 = "" + start + " " + lastScan;
		file.Write(str1);

	}

	@Metric(name = "performence_scema_digest")
	void Performence_schema_digest() {
		try {

			Statement st = con.createStatement();
			String sql = "SELECT *\r\n" + "  FROM performance_schema.events_statements_summary_by_digest\r\n"
					+ " WHERE schema_name IS NOT NULL GROUP BY schema_NAME";
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				while (rs.next()) {
					System.out.println(rs.getString(1) + "|" + rs.getString(2) + "|" + rs.getString(3) + "|"
							+ rs.getInt(4) + "|" + rs.getLong(5) + "|" + rs.getLong(6) + "|" + rs.getLong(7) + "|"
							+ rs.getLong(8) + "|" + rs.getLong(9) + "|" + rs.getLong(10) + "|" + rs.getInt(11) + "|"
							+ rs.getInt(12) + "|" + rs.getInt(13) + "|" + rs.getInt(14) + "|" + rs.getInt(15) + "|"
							+ rs.getInt(16) + "|" + rs.getInt(17) + "|" + rs.getInt(18) + "|" + rs.getInt(19) + "|"
							+ rs.getInt(20) + "|" + rs.getInt(21) + "|" + rs.getInt(22) + "|" + rs.getInt(23) + "|"
							+ rs.getInt(24) + "|" + rs.getInt(25) + "|" + rs.getInt(26) + "|" + rs.getInt(27) + "|"
							+ rs.getString(28) + "|" + rs.getString(29));
				}
			} else {
				System.out.println("Something went Wrong..!");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	@Metric(name = "slow_queries")
	void slow_Queries() {
		try {

			Statement st = con.createStatement();
			String sql = "SELECT * FROM sys.statements_with_runtimes_in_95th_percentile";
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				while (rs.next()) {
					System.out.println(rs.getString(1) + "|" + rs.getString(2) + "|" + rs.getString(3) + "|"
							+ rs.getString(4) + "|" + rs.getString(5) + "|" + rs.getString(6) + "|" + rs.getString(7)
							+ "|" + rs.getString(8) + "|" + rs.getString(9) + "|" + rs.getString(10) + "|"
							+ rs.getString(11) + "|" + rs.getString(12) + "|" + rs.getString(12));
				}
			} else {
				System.out.println("Something went Wrong..!");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	@Metric(name = "performence_errors")
	void performence_Error() {
		try {

			Statement st = con.createStatement();
			String sql = "SELECT schema_name\r\n" + "     , SUM(sum_errors) err_count\r\n"
					+ "  FROM performance_schema.events_statements_summary_by_digest\r\n"
					+ " WHERE schema_name IS NOT NULL\r\n" + " GROUP BY schema_name";
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				while (rs.next()) {
					System.out.println(rs.getString(1) + "----- " + rs.getInt(2));
				}
			} else {
				System.out.println("Something went Wrong..!");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	@Metric(name = "performence_avg_runtime")
	void performence_avg_runtime() {
		try {
			Statement st = con.createStatement();
			String sql = "SELECT schema_name\r\n" + "     , SUM(count_star) count\r\n"
					+ "     , ROUND(   (SUM(sum_timer_wait) / SUM(count_star))\r\n"
					+ "              / 1000000) AS avg_microsec\r\n"
					+ "  FROM performance_schema.events_statements_summary_by_digest\r\n"
					+ " WHERE schema_name IS NOT NULL\r\n" + " GROUP BY schema_name;";
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				while (rs.next()) {
					System.out.println(rs.getString(1) + "----- " + rs.getInt(2) + "----" + rs.getInt(3));
				}
			} else {
				System.out.println("Something went Wrong..!");
			}
		} catch (Exception e) {
			System.out.print(e);
		}

	}

	@Metric(name = "performence_warnings")
	void performence_Warnings() {
		try {
			Statement st = con.createStatement();
			String sql = "SELECT * FROM sys.statements_with_errors_or_warnings ORDER BY errors DESC LIMIT 10";
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				while (rs.next()) {
					System.out.println(rs.getString(1) + "----- " + rs.getString(2) + "----- " + rs.getString(3)
							+ "----- " + rs.getString(4) + "----- " + rs.getString(5) + "-----" + rs.getString(6)
							+ "----- " + rs.getString(7) + "----- " + rs.getString(8) + "----- " + rs.getString(9)
							+ "----- " + rs.getString(10));
				}

			} else {
				System.out.println("Something went Wrong..!");
			}
		} catch (Exception e) {
			System.out.print(e);
		}

	}

	@Metric(name = "noProcess")
	void noOfProcess() {
		int num = 0;
		try {

			Statement st = con.createStatement();
			String url = "show processlist";
			ResultSet rs = st.executeQuery(url);
			while (rs.next()) {
				num++;
			}
			System.out.println("number of processes running=" + num);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Metric(name = "slowConnections")
	void showConnectionMetrics() {
		int num = 0, m, s, h;
		String str = "";
		try {
			System.out.println("*****CONNECTION METRICS*****");
			Statement st = con.createStatement();
			String url = "SHOW GLOBAL STATUS LIKE 'Uptime'";
			ResultSet rs = st.executeQuery(url);
			while (rs.next()) {
				num = rs.getInt(2);
			}
			h = num / 3600;
			str = str + String.valueOf(h) + " hours,";
			m = (h % 3600) / 60;
			str = str + String.valueOf(m) + " minutes and ";
			s = (h % 3600) % 60;
			str = str + String.valueOf(s) + " seconds";
			System.out.println("MySQL uptime :" + str);
			url = "show status where `variable_name` = 'Threads_connected'";
			rs = st.executeQuery(url);
			while (rs.next()) {
				num = rs.getInt(2);
			}
			System.out.println("Number of Threads Conected:" + num);
			url = "show status where `variable_name` = 'max_used_connections'";
			rs = st.executeQuery(url);
			while (rs.next()) {
				num = rs.getInt(2);
			}
			System.out.println(
					"The maximum number of connections that have been in use simultaneously since the server started(max_used_connections):"
							+ num);
			url = "show status where `variable_name` = 'aborted_connects'";
			rs = st.executeQuery(url);
			while (rs.next()) {
				num = rs.getInt(2);
			}
			System.out.println("Number of aborted Connects:" + num);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Metric(name = "slowQueries")
	void showQueryMetrics() {
		int num = -1;
		System.out.println("\n\n******QUERY METRICS*****");
		try {
			Statement st = con.createStatement();
			String url = "show global status like 'slow_queries'";
			ResultSet rs = st.executeQuery(url);
			if (rs.next())
				num = rs.getInt(2);
			System.out.println("Number of Slow Queries=" + num);
			url = "show global status like 'select_full_join'";
			rs = st.executeQuery(url);
			if (rs.next())
				num = rs.getInt(2);
			System.out.println(
					"Number of joins that perform table scans because they do not use indexes(select_full_join)="
							+ num);
			url = "show global status like 'Created_tmp_disk_tables'";
			rs = st.executeQuery(url);
			if (rs.next())
				num = rs.getInt(2);
			System.out.println("Number of temporary tables created on spining disks=" + num);
			url = "show global status like 'handler_read'";
			rs = st.executeQuery(url);
			if (rs.next())
				num = rs.getInt(2);
			System.out.println("Number of full table scans=" + num);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Metric(name = "bufferMetrics")
	void showBufferMetrics() {
		int num = -1;
		System.out.println("\n\n******BUFFER,CACHE,LOCK METRICS*****");
		try {

			Statement st = con.createStatement();
			String url = "show global status like 'Innodb_row_lock_waits'";
			ResultSet rs = st.executeQuery(url);
			if (rs.next())
				num = rs.getInt(2);
			System.out
					.println("The number of times operations on InnoDB tables had to wait for a row      lock=" + num);
			url = "show global status like 'Innodb_buffer_pool_wait_free'";
			rs = st.executeQuery(url);
			if (rs.next())
				num = rs.getInt(2);
			System.out.println("Number of times InnoDB had to wait for memory pages to be flushed=" + num);
			url = "SHOW GLOBAL status like 'Open_tables'";
			rs = st.executeQuery(url);
			if (rs.next())
				num = rs.getInt(2);
			System.out.println("No:of tables opened=" + num);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
