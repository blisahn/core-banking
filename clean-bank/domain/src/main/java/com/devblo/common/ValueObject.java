package com.devblo.common;

/**
 * Marker interface for Value Objects.
 *
 * <h2>Value Object Özellikleri:</h2>
 * <ul>
 *   <li><b>Immutable:</b> Oluşturulduktan sonra değiştirilemez</li>
 *   <li><b>Equality by value:</b> Kimlik (ID) yerine değerlerine göre karşılaştırılır</li>
 *   <li><b>Self-validating:</b> Constructor'da kendi geçerliliğini kontrol eder</li>
 *   <li><b>Side-effect free:</b> Metodlar state değiştirmez, yeni instance döner</li>
 * </ul>
 *
 * <h2>Entity vs Value Object:</h2>
 * <pre>
 * Entity: Kimliği var (ID), zaman içinde değişebilir
 *         Örnek: Account, Customer
 *
 * Value Object: Kimliği yok, değerleri önemli, immutable
 *               Örnek: Money, Email, AccountNumber
 * </pre>
 *
 * <h2>Kullanım Örneği:</h2>
 * <pre>
 * public record Money(BigDecimal amount, Currency currency) implements ValueObject {
 *     public Money {
 *         if (amount.compareTo(BigDecimal.ZERO) < 0) {
 *             throw new InvalidMoneyException("Amount cannot be negative");
 *         }
 *     }
 *
 *     public Money add(Money other) {
 *         return new Money(amount.add(other.amount), currency); // Yeni instance döner!
 *     }
 * }
 * </pre>
 */
public interface ValueObject {
    // Marker interface - method gerekmez
}
