// Copyright by camLine GmbH, Germany, 2022
package com.fii.practic.mes.wip.general;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@RevisionEntity
@Table(name = EnversRevisionEntity.TABLE_NAME)
@SequenceGenerator(name = EnversRevisionEntity.ENTITY_NAME + ".IdSeq", sequenceName = EnversRevisionEntity.SEQUENCE_NAME, allocationSize = 1)
@Getter @Setter
public class EnversRevisionEntity {

	public static final String ENTITY_NAME = "Envers";
	public static final String TABLE_NAME = "T_ENVERS_REVINFO";
	public static final String SEQUENCE_NAME = "T_ENVERS_SEQ__ID";

	@Id
	@Column(name = "ID", nullable = false, unique = true)
	@RevisionNumber
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = EnversRevisionEntity.ENTITY_NAME + ".IdSeq")
	private long id;

	@RevisionTimestamp
	@Column(name = "TIME_STAMP", nullable = false)
	private LocalDateTime timestamp;

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof EnversRevisionEntity)) {
			return false;
		}

		final EnversRevisionEntity that = (EnversRevisionEntity) o;

		if (id != that.id) {
			return false;
		}
		return timestamp.equals(that.timestamp);
	}

	@Override
	public int hashCode() {
		int result;
		result = String.valueOf(id).hashCode();
		result = 31 * result + (timestamp.getDayOfMonth() * timestamp.getMonthValue() * timestamp.getYear());
		return result;
	}

	@Override
	public String toString() {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		return "DefaultRevisionEntity(id = " + id + ", revisionDate = "
				+ dateTimeFormatter.format(timestamp) + ")";
	}
}
