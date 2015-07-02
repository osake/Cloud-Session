/**
 * This class is generated by jOOQ
 */
package com.parallax.server.common.cloudsession.db.generated.tables;


import com.parallax.server.common.cloudsession.db.generated.Cloudsession;
import com.parallax.server.common.cloudsession.db.generated.Keys;
import com.parallax.server.common.cloudsession.db.generated.tables.records.BucketRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.1"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Bucket extends TableImpl<BucketRecord> {

	private static final long serialVersionUID = 1088684251;

	/**
	 * The reference instance of <code>cloudsession.bucket</code>
	 */
	public static final Bucket BUCKET = new Bucket();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<BucketRecord> getRecordType() {
		return BucketRecord.class;
	}

	/**
	 * The column <code>cloudsession.bucket.id</code>.
	 */
	public final TableField<BucketRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>cloudsession.bucket.id_user</code>.
	 */
	public final TableField<BucketRecord, Long> ID_USER = createField("id_user", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>cloudsession.bucket.type</code>.
	 */
	public final TableField<BucketRecord, String> TYPE = createField("type", org.jooq.impl.SQLDataType.VARCHAR.length(45).nullable(false), this, "");

	/**
	 * The column <code>cloudsession.bucket.content</code>.
	 */
	public final TableField<BucketRecord, Integer> CONTENT = createField("content", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>cloudsession.bucket.timestamp</code>.
	 */
	public final TableField<BucketRecord, Timestamp> TIMESTAMP = createField("timestamp", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaulted(true), this, "");

	/**
	 * Create a <code>cloudsession.bucket</code> table reference
	 */
	public Bucket() {
		this("bucket", null);
	}

	/**
	 * Create an aliased <code>cloudsession.bucket</code> table reference
	 */
	public Bucket(String alias) {
		this(alias, BUCKET);
	}

	private Bucket(String alias, Table<BucketRecord> aliased) {
		this(alias, aliased, null);
	}

	private Bucket(String alias, Table<BucketRecord> aliased, Field<?>[] parameters) {
		super(alias, Cloudsession.CLOUDSESSION, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<BucketRecord, Long> getIdentity() {
		return Keys.IDENTITY_BUCKET;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<BucketRecord> getPrimaryKey() {
		return Keys.KEY_BUCKET_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<BucketRecord>> getKeys() {
		return Arrays.<UniqueKey<BucketRecord>>asList(Keys.KEY_BUCKET_PRIMARY, Keys.KEY_BUCKET_USER_TYPE_UNIQUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<BucketRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<BucketRecord, ?>>asList(Keys.BUCKET_USER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bucket as(String alias) {
		return new Bucket(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Bucket rename(String name) {
		return new Bucket(name, null);
	}
}
